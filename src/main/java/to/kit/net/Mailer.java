package to.kit.net;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import to.kit.net.struct.Preference;

/**
 * メール送信.
 * @author Hidetaka Sasai
 */
public final class Mailer {
	/**
	 * メールを送信.
	 * @param subject
	 * @param body
	 * @throws EmailException
	 */
	public void send(String subject, String body) throws EmailException {
		Preference pref = Preference.Instance;
		String mailFrom = pref.getMailFrom();
		SimpleEmail email = new SimpleEmail();

		email.setHostName(pref.getSmtpHost());
		email.setSmtpPort(587);
		email.setAuthentication(pref.getSmtpUser(), pref.getSmtpPass());
		email.setStartTLSEnabled(true);
		email.setFrom(mailFrom);
		email.setSubject(subject);
		email.setMsg(body);
		email.setCharset("ISO-2022-JP");
		email.addTo(pref.getMailTo());
		email.addCc(mailFrom);
		email.send();
	}
}
