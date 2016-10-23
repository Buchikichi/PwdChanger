package to.kit.net.struct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import net.arnx.jsonic.JSON;

/**
 * Preference.
 * @author Hidetaka Sasai
 */
public final class Preference {
	/** 唯一のインスタンス. */
	public static Preference Instance = new Preference();

	private String uri;
	private String loginId;
	private String passwd;
	private String userId;
	private String userPasswd;
	private String userAgent;

	private String passwdPrefix;
	private String passwdAction;

	private String smtpHost;
	private String smtpUser;
	private String smtpPass;
	private String mailFrom;
	private String mailTo;

	private Preference() {
		// nop
	}

	/**
	 * 現在のパスワードを取得.
	 * @return プレフィックス + 値
	 */
	public String getCurrentPasswd() {
		return this.passwdPrefix + this.passwd;
	}

	/**
	 * Load preference.
	 * @param name
	 * @throws IOException
	 */
	public void load(String name) throws IOException {
		try (InputStream in = new FileInputStream(new File(name))) {
			Preference pref = JSON.decode(in, Preference.class);

			try {
				PropertyUtils.copyProperties(this, pref);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Save preference.
	 * @param name
	 * @throws IOException
	 */
	public void save(String name) throws IOException {
		try (Writer writer = new FileWriter(new File(name))) {
			String json = JSON.encode(this, true);

			writer.write(json);
		}
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return this.uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return this.loginId;
	}

	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return the passwd
	 */
	public String getPasswd() {
		return this.passwd;
	}

	/**
	 * @param passwd the passwd to set
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userPasswd
	 */
	public String getUserPasswd() {
		return this.userPasswd;
	}

	/**
	 * @param userPasswd the userPasswd to set
	 */
	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return this.userAgent;
	}

	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * @return the passwdPrefix
	 */
	public String getPasswdPrefix() {
		return this.passwdPrefix;
	}

	/**
	 * @param passwdPrefix the passwdPrefix to set
	 */
	public void setPasswdPrefix(String passwdPrefix) {
		this.passwdPrefix = passwdPrefix;
	}

	/**
	 * @return the passwdAction
	 */
	public String getPasswdAction() {
		return this.passwdAction;
	}

	/**
	 * @param passwdAction the passwdAction to set
	 */
	public void setPasswdAction(String passwdAction) {
		this.passwdAction = passwdAction;
	}

	/**
	 * @return the smtpHost
	 */
	public String getSmtpHost() {
		return this.smtpHost;
	}

	/**
	 * @param smtpHost the smtpHost to set
	 */
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	/**
	 * @return the smtpUser
	 */
	public String getSmtpUser() {
		return this.smtpUser;
	}

	/**
	 * @param smtpUser the smtpUser to set
	 */
	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}

	/**
	 * @return the smtpPass
	 */
	public String getSmtpPass() {
		return this.smtpPass;
	}

	/**
	 * @param smtpPass the smtpPass to set
	 */
	public void setSmtpPass(String smtpPass) {
		this.smtpPass = smtpPass;
	}

	/**
	 * @return the mailFrom
	 */
	public String getMailFrom() {
		return this.mailFrom;
	}

	/**
	 * @param mailFrom the mailFrom to set
	 */
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	/**
	 * @return the mailTo
	 */
	public String getMailTo() {
		return this.mailTo;
	}

	/**
	 * @param mailTo the mailTo to set
	 */
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
}
