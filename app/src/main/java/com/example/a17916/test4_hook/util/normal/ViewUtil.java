package com.example.a17916.test4_hook.util.normal;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.a17916.test4_hook.util.webview.RobotiumWebClient;
import com.example.a17916.test4_hook.view_data.MyViewNode;

import java.util.ArrayList;
import java.util.List;

public class ViewUtil {
    Activity currentActivity;
    View root;
    String webContent = "";

    public ViewUtil(Activity currentActivity) {
        this.currentActivity = currentActivity;
        root = currentActivity.getWindow().getDecorView();
    }

    public void setWebContent(String webContent) {
        this.webContent = webContent;
    }

    public String getWebContent() {

        return webContent;
    }

    public static String getLast(String name){
        String[] words = name.split("\\.");
        return  words[words.length-1];
    }

    public void getWebContent(final WebView root) {

        root.getSettings().setJavaScriptEnabled(true);

        //enable our WebChromeClient to deal with prompt function
        WebChromeClient currentWebChromeClient = getCurrentWebChromeClient(root);
        RobotiumWebClient robotiumWebCLient = new RobotiumWebClient(currentActivity, this);
        WebChromeClient originalWebChromeClient = null ;

        if(currentWebChromeClient != null && !currentWebChromeClient.getClass().isAssignableFrom(RobotiumWebClient.class)){
            originalWebChromeClient = currentWebChromeClient;
        }

        robotiumWebCLient.enableJavascriptAndSetRobotiumWebClient(root, originalWebChromeClient);



        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                root.loadUrl("javascript:prompt('ias' + document.getElementsByTagName(\"html\")[0].outerHTML);");
            }
        });

    }
    private WebChromeClient getCurrentWebChromeClient(WebView webview){
        WebChromeClient currentWebChromeClient = null;

        Object currentWebView = webview;

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            try{
                currentWebView = new Reflect(currentWebView).field("mProvider").out(Object.class);
            }catch(IllegalArgumentException ignored) {}
        }

        try{
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                Object mClientAdapter = new Reflect(currentWebView).field("mContentsClientAdapter").out(Object.class);
                currentWebChromeClient = new Reflect(mClientAdapter).field("mWebChromeClient").out(WebChromeClient.class);
            }
            else {
                Object mCallbackProxy = new Reflect(currentWebView).field("mCallbackProxy").out(Object.class);
                currentWebChromeClient = new Reflect(mCallbackProxy).field("mWebChromeClient").out(WebChromeClient.class);
            }
        }catch(Exception ignored){}

        return currentWebChromeClient;
    }

    private boolean isDerivedFrom(Class c, Class p) {
        while(c != Object.class) {
            if(c == p)
                return true;
            c = c.getSuperclass();
        }
        return false;
    }

    /**
     * 按照path提供的路径寻找指定的view
     * @param path 由从根view到当前view的简单类名组成，中间用分隔符隔开
     * @return
     */
    public View getViewbyPath(String path){


        List<View> targetViews = new ArrayList<View>();
        int index = -1;
        //如果有数字的，后面跟的第一个View的class是子节点的
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] pathNodes = path.split("/");
        int begin = 0;
        //标准的xpath路径，起始节点就是root，应该删掉
        begin = 1;
        List<List<View>> tmpViews = new ArrayList<>();
        for(int i = 1; i < pathNodes.length; i++){
            tmpViews.add(new ArrayList<View>());
        }
        //默认根节点应该都是ViewGroup，可以这么认为？
        for(int i = 0; i < ((ViewGroup) root).getChildCount(); i++){
            View tmp = ((ViewGroup) root).getChildAt(i);
            tmpViews.get(0).add(tmp);
        }
        for(int i = 0; begin < pathNodes.length; begin++,i++){
            List<View> views = tmpViews.get(i);
            if(begin == pathNodes.length - 1){
                if(pathNodes[begin].contains("^")){
                    index = Integer.parseInt(pathNodes[begin].substring(pathNodes[begin].indexOf("^") + 1));
                }
            }
            for(int j = 0; j < views.size(); j++){
                View tmp = views.get(j);
                if(pathNodes[begin].startsWith(getViewName(tmp))){
                    //如果和path一致
                    if(begin < pathNodes.length - 1){
                        if(tmp instanceof ViewGroup){
                            for(int k = 0; k < ((ViewGroup) tmp).getChildCount(); k++){
                                tmpViews.get(i + 1).add(((ViewGroup) tmp).getChildAt(k));
                            }
                        }
                    }else{
                        //最后一层了，所以如果符合就直接放到结果的列表里面
                        targetViews.add(tmp);
                    }
                }
            }
        }
        if(targetViews == null || targetViews.isEmpty())
            return null;

        //默认就返回第一个（我们可以假设只会有一个匹配上吗？）
        if(index >= 0) {
            return targetViews.get(index - 1);
        }
        return targetViews.get(0);
    }

    String getViewName(View view){
        String name = view.getClass().getName();
        if(name.lastIndexOf(".") >= 0){
            name = name.substring(name.lastIndexOf(".")+1);
        }
        return name;
    }

    String getViewPath(MyViewNode view){
        String result = "";
        while(view != null){
            result = view.getView().getClass().getSimpleName() + "/"+ result;
            view = view.getParent();
        }
        return result;
    }

    public String getContentByWebPath(String path){


        if(!TextUtils.isEmpty(webContent))
            return webContent;
        List<View> views = new ArrayList<View>();
        views.add(root);
        WebView webView = null;
        while(views.size() > 0){
            View now = views.remove(0);
            if(isDerivedFrom(now.getClass(), WebView.class)){
                webView = (WebView) now;
                break;
            }
            if(now instanceof ViewGroup){
                for(int i = 0;i < ((ViewGroup) now).getChildCount();i++){
                    views.add(((ViewGroup) now).getChildAt(i));
                }
            }
        }
        if(webView !=null) {
            getWebContent(webView);
        }

        return webContent;
    }

    /**
     * 大体与getViewbyPath差不多，可以先获得View在获得text
     * @param path
     * @return
     */
    public String getContentbyPath(String path){

        List<View> targetViews = new ArrayList<View>();

        int index = -1;
        //如果有数字的，后面跟的第一个View的class是子节点的
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] pathNodes = path.split("/");
        List<List<View>> tmpViews = new ArrayList<>();
        for(int i = 1; i < pathNodes.length; i++){
            tmpViews.add(new ArrayList<View>());
        }
        //默认根节点应该都是ViewGroup，可以这么认为？
        for(int i = 0; i < ((ViewGroup) root).getChildCount(); i++){
            View tmp = ((ViewGroup) root).getChildAt(i);
            tmpViews.get(0).add(tmp);
        }
        for(int i = 1; i < pathNodes.length; i++){

            List<View> views = tmpViews.get(i - 1);

            if(i == pathNodes.length - 1){
                if(pathNodes[i].contains("^")){
                    index = Integer.parseInt(pathNodes[i].substring(pathNodes[i].indexOf("^") + 1));
                }
            }

            for(int j = 0; j < views.size(); j++){
                View tmp = views.get(j);
                if(pathNodes[i].startsWith(getViewName(tmp))){
                    //如果和path一致
                    if(i < pathNodes.length - 1){
                        if(tmp instanceof ViewGroup){
                            for(int k = 0; k < ((ViewGroup) tmp).getChildCount(); k++){
                                tmpViews.get(i).add(((ViewGroup) tmp).getChildAt(k));
                            }
                        }
                    }else{
                        //最后一层了，所以如果符合就直接放到结果的列表里面
                        targetViews.add(tmp);
                    }
                }
            }
        }
        String content = "";
        //默认就返回第一个（我们可以假设只会有一个匹配上吗？）
        if(index >= 0) {
            if(targetViews.size() >= index) {
                View tmp = targetViews.get(index - 1);
                if (tmp instanceof TextView) {
                    content = ((TextView) tmp).getText().toString().trim();
                }
            }
        }else{
            for(View tmp : targetViews){
                if(tmp instanceof TextView){
                    content += ((TextView) tmp).getText().toString().trim();
                }
            }
        }
        return content;
    }

    public String getContentById(String id){

        String content = "";
        int view_id = currentActivity.getResources().getIdentifier(id, null, null);
        if(view_id > 0) {
            View view = root.findViewById(view_id);
            if (view != null && view instanceof TextView) {
                content += ((TextView) view).getText().toString().trim();
            }
        }
        return content;
    }

    public View getViewById(String id){

        int view_id = currentActivity.getResources().getIdentifier(id, null, null);
        if(view_id > 0) {
            View view = root.findViewById(view_id);
            return view;
        }
        return null;
    }

    public static boolean fromRecyclerView(Class target){

        Class r = getRecyclerView();
        if(r == null)
            return false;
        return r.isAssignableFrom(target);
    }
    //由于有些应用中没有用到RecyclerView，如果直接用RecyclerView会报找不到类的错误，所以需要用反射的方式
    public static Class getRecyclerView(){
        Class r = null;
        try {
            r = Class.forName("android.support.v7.widget.RecyclerView");
        } catch (ClassNotFoundException e) {
            return null;
        }
        return r;
    }
    public static String getFieldName(Context context, String package_name, int id){
        String result = null;
        try {
            result = context.getResources().getResourceEntryName(id);
        }catch (Resources.NotFoundException e){
            e.printStackTrace();
        }
        Log.v("liuyi", "id name: " + result);
        return result;
    }
    public static int getViewCountInActivity(View view){
        int count = 0;
        ViewGroup viewGroup;
        View childView;
        if(view instanceof ViewGroup){
            viewGroup = (ViewGroup) view;

            int childCount = viewGroup.getChildCount();
            for(int i=0;i<childCount;i++){
                childView = viewGroup.getChildAt(i);
                count+=getViewCountInActivity(childView);
            }
        }else{
            return 1;
        }
        return count;
    }
}
