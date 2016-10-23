package to.kit;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import to.kit.net.Crawler;
import to.kit.net.Mailer;
import to.kit.net.ZkPasswd;
import to.kit.net.struct.DocumentInfo;
import to.kit.net.struct.FormInfo;
import to.kit.net.struct.Preference;

/**
 * パスワードチェンジャー.
 * @author Hidetaka Sasai
 */
public class PwdChangerMain {
	private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);
	private static final String PREFERENCE_FILE = "preference.json";

	private void sendmail(String passwd) throws EmailException {
		String subject = "Regarding the change password.";
		String num = StringUtils.right(passwd, 2);
		String body = "パスワードを[" + num + "]に変更しました。";

		new Mailer().send(subject, body);
	}

	private void execute() throws IOException, SAXException, URISyntaxException, EmailException {
		Preference pref = Preference.Instance;
		Crawler crawler = new Crawler();
		DocumentInfo doc = crawler.getDocument(new HttpGet(pref.getUri()));
		String currentNumber = pref.getPasswd();
		String passwd = ZkPasswd.Instance.getCurrentPassword();

		if (currentNumber.equals(passwd)) {
			LOG.info("The password is not changed.");
			return;
		}
		String currentPasswd = pref.getCurrentPasswd();
		FormInfo form = doc.getForm();
		form.put("login_id", pref.getLoginId());
		form.put("passwd", currentPasswd);
		doc = crawler.submit(form);
		LOG.info("login1: success.");
		//
		form = doc.getForm();
		form.put("user_id", pref.getUserId());
		form.put("user_passwd", pref.getUserPasswd());
		doc = crawler.submit(form);
		LOG.info("login2: success.");
		// [次へ]ボタン
		form = doc.getForm();
		doc = crawler.submit(form);
		LOG.info("[next]: success.");
		// [パスワード変更]ボタン
		String link = doc.getLink(pref.getPasswdAction());
		doc = crawler.getDocument(new HttpGet(link));
		// [変更]ボタン
		String prefix = pref.getPasswdPrefix();
		String newPasswd = prefix + passwd;
		LOG.info("new_passwd:" + passwd);
		form = doc.getForm();
		form.put("current_passwd", currentPasswd);
		form.put("new_passwd", newPasswd);
		form.put("new_passwd_again", newPasswd);
		LOG.info(form.toString());
		doc = crawler.submit(form);
		sendmail(passwd);
		pref.setPasswd(passwd);
	}

	/**
	 * パスワードチェンジャーメイン.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Preference pref = Preference.Instance;
		PwdChangerMain app = new PwdChangerMain();

		pref.load(PREFERENCE_FILE);
		app.execute();
		pref.save(PREFERENCE_FILE);
	}
}
