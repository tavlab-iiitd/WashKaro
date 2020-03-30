package inspire2connect.inspire2connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

public class privacy_policy extends AppCompatActivity {
    WebView webView;

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        //return super.onSupportNavigateUp();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_privacy_policy);
        webView = (WebView) findViewById(R.id.webview_policy);
        webView.loadData("<!DOCTYPE html>\n" +
                "    <html>\n" +
                "    <head>\n" +
                "      <meta charset='utf-8'>\n" +
                "      <meta name='viewport' content='width=device-width'>\n" +
                "      <title>Privacy Policy</title>\n" +
                "      <style> body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; padding:1em; } </style>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "    <h2>Privacy Policy</h2> <p>\n" +
                "                    Tav Lab built the WashKaro app as\n" +
                "                    a Free app. This SERVICE is provided by\n" +
                "                    Tav Lab at no cost and is intended for\n" +
                "                    use as is.\n" +
                "                  </p> <p>\n" +
                "                    This page is used to inform visitors regarding\n" +
                "                    our policies with the collection, use, and\n" +
                "                    disclosure of Personal Information if anyone decided to use\n" +
                "                    our Service.\n" +
                "                  </p> <p>\n" +
                "                    If you choose to use our Service, then you agree\n" +
                "                    to the collection and use of information in relation to this\n" +
                "                    policy. The Personal Information that we collect is\n" +
                "                    used for providing and improving the Service.\n" +
                "                    We will not use or share your\n" +
                "                    information with anyone except as described in this Privacy\n" +
                "                    Policy.\n" +
                "                  </p> <p>\n" +
                "                    The terms used in this Privacy Policy have the same meanings\n" +
                "                    as in our Terms and Conditions, which is accessible at\n" +
                "                    WashKaro unless otherwise defined in this Privacy\n" +
                "                    Policy.\n" +
                "                  </p> <p><strong>Information Collection and Use</strong></p> <p>\n" +
                "                    For a better experience, while using our Service,\n" +
                "                    we may require you to provide us with certain\n" +
                "                    personally identifiable information, including but not limited to Anonymised Location. The\n" +
                "                    information that we request will be\n" +
                "                    retained by us and used as described in this privacy policy.\n" +
                "                  </p> <p>\n" +
                "                    The app does use third party services that may collect\n" +
                "                    information used to identify you.\n" +
                "                  </p> <div><p>\n" +
                "                      Link to privacy policy of third party service providers\n" +
                "                      used by the app\n" +
                "                    </p> <ul><li><a href=\"https://www.google.com/policies/privacy/\" target=\"_blank\">Google Play Services</a></li><!----><li><a href=\"https://firebase.google.com/policies/analytics\" target=\"_blank\">Google Analytics for Firebase</a></li><!----><!----><!----><!----><!----><!----><!----><!----><!----><!----><!----><!----></ul></div> <p><strong>Log Data</strong></p> <p>\n" +
                "                    We want to inform you that whenever\n" +
                "                    you use our Service, in a case of an error in the\n" +
                "                    app we collect data and information (through third\n" +
                "                    party products) on your phone called Log Data. This Log Data\n" +
                "                    may include information such as your device Internet\n" +
                "                    Protocol (“IP”) address, device name, operating system\n" +
                "                    version, the configuration of the app when utilizing\n" +
                "                    our Service, the time and date of your use of the\n" +
                "                    Service, and other statistics.\n" +
                "                  </p> <p><strong>Cookies</strong></p> <p>\n" +
                "                    Cookies are files with a small amount of data that are\n" +
                "                    commonly used as anonymous unique identifiers. These are\n" +
                "                    sent to your browser from the websites that you visit and\n" +
                "                    are stored on your device's internal memory.\n" +
                "                  </p> <p>\n" +
                "                    This Service does not use these “cookies” explicitly.\n" +
                "                    However, the app may use third party code and libraries that\n" +
                "                    use “cookies” to collect information and improve their\n" +
                "                    services. You have the option to either accept or refuse\n" +
                "                    these cookies and know when a cookie is being sent to your\n" +
                "                    device. If you choose to refuse our cookies, you may not be\n" +
                "                    able to use some portions of this Service.\n" +
                "                  </p> <p><strong>Service Providers</strong></p> <p>\n" +
                "                    We may employ third-party companies\n" +
                "                    and individuals due to the following reasons:\n" +
                "                  </p> <ul><li>To facilitate our Service;</li> <li>To provide the Service on our behalf;</li> <li>To perform Service-related services; or</li> <li>To assist us in analyzing how our Service is used.</li></ul> <p>\n" +
                "                    We want to inform users of this\n" +
                "                    Service that these third parties have access to your\n" +
                "                    Personal Information. The reason is to perform the tasks\n" +
                "                    assigned to them on our behalf. However, they are obligated\n" +
                "                    not to disclose or use the information for any other\n" +
                "                    purpose.\n" +
                "                  </p> <p><strong>Security</strong></p> <p>\n" +
                "                    We value your trust in providing us\n" +
                "                    your Personal Information, thus we are striving to use\n" +
                "                    commercially acceptable means of protecting it. But remember\n" +
                "                    that no method of transmission over the internet, or method\n" +
                "                    of electronic storage is 100% secure and reliable, and\n" +
                "                    we cannot guarantee its absolute security.\n" +
                "                  </p> <p><strong>Links to Other Sites</strong></p> <p>\n" +
                "                    This Service may contain links to other sites. If you click\n" +
                "                    on a third-party link, you will be directed to that site.\n" +
                "                    Note that these external sites are not operated by\n" +
                "                    us. Therefore, we strongly advise you to\n" +
                "                    review the Privacy Policy of these websites.\n" +
                "                    We have no control over and assume no\n" +
                "                    responsibility for the content, privacy policies, or\n" +
                "                    practices of any third-party sites or services.\n" +
                "                  </p> <p><strong>Children’s Privacy</strong></p> <p>\n" +
                "                    These Services do not address anyone under the age of 13.\n" +
                "                    We do not knowingly collect personally\n" +
                "                    identifiable information from children under 13. In the case\n" +
                "                    we discover that a child under 13 has provided\n" +
                "                    us with personal information,\n" +
                "                    we immediately delete this from our servers. If you\n" +
                "                    are a parent or guardian and you are aware that your child\n" +
                "                    has provided us with personal information, please contact\n" +
                "                    us so that we will be able to do\n" +
                "                    necessary actions.\n" +
                "                  </p> <p><strong>Changes to This Privacy Policy</strong></p> <p>\n" +
                "                    We may update our Privacy Policy from\n" +
                "                    time to time. Thus, you are advised to review this page\n" +
                "                    periodically for any changes. We will\n" +
                "                    notify you of any changes by posting the new Privacy Policy\n" +
                "                    on this page. These changes are effective immediately after\n" +
                "                    they are posted on this page.\n" +
                "                  </p> <p><strong>Contact Us</strong></p> <p>\n" +
                "                    If you have any questions or suggestions about\n" +
                "                    our Privacy Policy, do not hesitate to contact\n" +
                "                    us at tavlab@iiitd.ac.in.\n" +
                "                  </p> <p>\n" +
                "                    This privacy policy page was created at\n" +
                "                    <a href=\"https://privacypolicytemplate.net\" target=\"_blank\">privacypolicytemplate.net</a>\n" +
                "                    and modified/generated by\n" +
                "                    <a href=\"https://app-privacy-policy-generator.firebaseapp.com/\" target=\"_blank\">App Privacy Policy Generator</a></p>\n" +
                "    </body>\n" +
                "    </html>", "text/html", "UTF-8");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lang_togg_butt) {
            Toast.makeText(privacy_policy.this, "Language Changed", Toast.LENGTH_SHORT).show();
            //switch_language();


        } else if (id == R.id.Survey) {
            Intent i = new Intent(privacy_policy.this, Male_Female.class);
            startActivity(i);
        } else if (id == R.id.developers) {
            Intent i = new Intent(privacy_policy.this, about.class);
            startActivity(i);
        } else if (id == R.id.privacy_policy) {
            Intent i = new Intent(privacy_policy.this, privacy_policy.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

}
