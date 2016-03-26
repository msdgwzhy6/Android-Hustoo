package com.scb.administrator.a.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scb.administrator.a.R;
import com.scb.administrator.a.adapter.BDAdapter;
import com.scb.administrator.a.adapter.BookAdapter;
import com.scb.administrator.a.adapter.MyAdapter;
import com.scb.administrator.a.adapter.NoticeAdapter;
import com.scb.administrator.a.entity.Book;
import com.scb.administrator.a.entity.BookAdd;
import com.scb.administrator.a.util.NetworkUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import dmax.dialog.SpotsDialog;

public class BookActivity extends AppCompatActivity {




    private ListView mListView;
    private List<Book> mBooks;
   private BDAdapter bdAdapter;
    private BookAdapter mAdapter;
     private  TextView title;
    private ListView addListView;
    private  List<BookAdd> madd;
   private ProgressBar pb;
    private SpotsDialog dialog2;
 private  MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Toolbar mToolbar;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.book_list);


        mBooks = new ArrayList<>();
        initData();
        mAdapter = new BookAdapter(BookActivity.this, mBooks);
        mListView.setAdapter(mAdapter);
        madd = new ArrayList<BookAdd>();



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(!NetworkUtils.isNetworkConnected(BookActivity.this)) {
                    Toast.makeText(BookActivity.this, "请一下检查网络", Toast.LENGTH_SHORT).show();
                    return;
                }


                dialog = new MaterialDialog.Builder(BookActivity.this)
                        .customView(R.layout.dialog_book, true)
                        .build();
                if(dialog!=null) {

                    title = (TextView) dialog.getCustomView().findViewById(R.id.book_name);
                   pb = (ProgressBar) dialog.getCustomView().findViewById(R.id.progress_bar);
                addListView = (ListView) dialog.getCustomView().findViewById(R.id.book_add);
                    bdAdapter = new BDAdapter(BookActivity.this,madd);

                   addListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                       @Override
                       public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                           BookAdd ba1 = bdAdapter.getItem(position);
                           ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                           c.setText("书名：" + mAdapter.getItem(position).getName() + "\n馆藏地址：" + ba1.getO()
                                           + "\n索书号：" + ba1.getT() + "\n状态：" + ba1.getThr()
                           );
                           Toast.makeText(BookActivity.this, "已将图书信息复制到剪贴板", Toast.LENGTH_SHORT).show();
                           return true;
                       }
                   });
                        addListView.setAdapter(bdAdapter);
                     madd.clear();
                    title.setText("书名 : " +  mAdapter.getItem(position).getName());

                        dialog.show();
                    new myTask().execute( mAdapter.getItem(position).getLink());



                }




            }
        });

    }

    private void initData() {
       String [] bookPic={"http://202.114.9.17/bibimage/zycover.php?isbn=9787201080987",
                         "http://202.114.9.17/bibimage/zycover.php?isbn=9787532152759",
               "http://202.114.9.17/bibimage/zycover.php?isbn=9787532749522",
              "http://202.114.9.17/bibimage/zycover.php?isbn=9787532153756",
               "http://202.114.9.17/bibimage/zycover.php?isbn=9787563379071",
           "http://202.114.9.17/bibimage/zycover.php?isbn=9787506365680",


                          };
        String [] books= {"像少年啦飞驰 / 韩寒","渴求真爱的幽灵 /  文野村美月","十一字杀人 / (日) 东野圭吾著","替身S /  绫辻行人","原来你非不快乐 /  林夕 ","许三观卖血记 / 余华"};
        String [] booksLink = {"http://ftp.lib.hust.edu.cn/search*chx?/Xandroid&SORT=DZ/Xandroid&SORT=DZ&extended=0&SUBKEY=android/1%2C312%2C312%2CB/frameset&FF=Xandroid&SORT=DZ&10%2C10%2C",
                                "http://ftp.lib.hust.edu.cn/record=b2799024*chx",
                               "http://ftp.lib.hust.edu.cn/record=b2125605*chx",
                           "http://ftp.lib.hust.edu.cn/record=b2759620*chx",
                           "http://ftp.lib.hust.edu.cn/record=b2105016*chx",
                        "http://ftp.lib.hust.edu.cn/record=b2597102*chx",

                               };
        for(int i = 0 ; i<=5;i++){
            mBooks.add(new Book(books[i],bookPic[i],booksLink[i]));
        }

    }

    private void setupSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchMenuItem
                .getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("图书搜索");
        if (searchManager != null && searchView != null) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));

            searchView
                    .setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {

                            if (!hasFocus) {
                                if (searchMenuItem != null) {
                                    searchMenuItem.collapseActionView();
                                }// end if
                                if (searchView != null) {
                                    searchView.onActionViewCollapsed();
                                }// end if
                            }// end if
                        }
                    });

            searchView
                    .setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            /**
                             * hides and then unhides search tab to make sure
                             * keyboard disappears when query is submitted
                             */
                            if (searchView != null&&NetworkUtils.isNetworkConnected(BookActivity.this)) {
                                searchView.setVisibility(View.INVISIBLE);
                                searchView.setVisibility(View.VISIBLE);
                                fetchData(query);
                            }
                            else  Toast.makeText(BookActivity.this, "请一下检查网络", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            // TODO Auto-generated method stub
                            return false;
                        }
                    });


        }
    }

    private void fetchData(String s) {

        String url = null;
        s =  java.net.URLEncoder.encode(s);

            url = "http://ftp.lib.hust.edu.cn/search*chx/X?SEARCH="+s;
            dialog2 = new SpotsDialog(BookActivity.this,R.style.Custom);
            dialog2.show();
            new myTask2().execute(url);



    }


    @Override
            public boolean onCreateOptionsMenu (Menu menu){

                getMenuInflater().inflate(R.menu.menu_main, menu);
                setupSearchView(menu);
                return super.onCreateOptionsMenu(menu);
            }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    class myTask extends AsyncTask<String , Void ,List<BookAdd>>{

        @Override
        protected void onPostExecute(List<BookAdd> bookAdds) {
            super.onPostExecute(bookAdds);





            if(bookAdds.size()==0){
             madd.add(new BookAdd("无网络", "无网络", "无网络"));


            }


            bdAdapter.refresh(bookAdds);
            setListViewHeightBasedOnChildren(addListView);
            pb.setVisibility(ProgressBar.GONE);



        }

        @Override
        protected List<BookAdd> doInBackground(String... params) {

            List<BookAdd> ba = new ArrayList<>();
            String url = params[0];
            Connection conn = Jsoup.connect(url);
            String show="";
            conn.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
            try {
                Document doc2 = conn.get();
                Elements elements2 = doc2.getElementsByClass("bibItemsEntry");
                for(Element v :elements2 ){
                    String c = v.getElementsByTag("a").text();
                    String s = v.getElementsByTag("td").first().text();
                    String d = v.getElementsByTag("td").last().text();
                   BookAdd ad = new BookAdd(s,c,d);
                    ba.add(ad);
                }

                if(elements2.size()==0){

                    Element element = doc2.getElementsByClass("bibOrderEntry").first();
                    if(element!=null) {
                        String f = element.getElementsByTag("td").first().text();
                        BookAdd ad = new BookAdd("无\n\n", f, "无\n\n");
                        ba.add(ad);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



            return ba;
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }
        totalHeight*=1.4;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount()-1))
                +15;
        listView.setLayoutParams(params);
    }

    class myTask2 extends AsyncTask<String , Void ,List<Book>>{

        @Override
        protected List<Book> doInBackground(String... params) {

            String url = params[0];

            Connection conn = Jsoup.connect(url);
            conn.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
            List<Book>  books = new ArrayList<>();
            try {
                Document doc = conn.get();
                Elements elements = doc.getElementsByClass("briefcitTitle");
               int v=0;
                for(Element element : elements) {



                    String address =  "http://ftp.lib.hust.edu.cn"+element.getElementsByTag("a").attr("href");
                    String name = element.text();


                    Book book  = new Book(name," ",address);
                    books.add(book);
                    if(v==30) break;
                    v++;


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Document doc2;
            try {
                doc2 = conn.get();
                Elements media = doc2.select("[src]");

                int i = 0;
                for (Element src : media) {

                    if (src.tagName().equals("img")&& src.attr("alt").equals("书的封面"))
                    {

                        String img =  src.attr("abs:src") ;
                        books.get(i).setPic(img);
                        if(i==30) break;
                        i++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return books;
        }

        @Override
        protected void onPostExecute(List<Book> books) {


            mAdapter.refresh(books);
            if(dialog2!=null)
                dialog2.dismiss();
            super.onPostExecute(books);
        }
    }

        }

