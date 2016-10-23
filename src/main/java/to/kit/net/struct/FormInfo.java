package to.kit.net.struct;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.html.HTMLFormElement;

/**
 * フォーム情報.
 * @author Hidetaka Sasai
 */
public final class FormInfo implements Iterable<NameValuePair> {
	private HTMLFormElement form;
	private List<NameValuePair> paramList = new ArrayList<>();

	/**
	 * フォームに値を設定.
	 * @param name
	 * @param value
	 */
	public void put(String name, String value) {
		BasicNameValuePair target = null;

		for (NameValuePair nameValue : this.paramList) {
			if (name.equals(nameValue.getName())) {
				target = (BasicNameValuePair) nameValue;
				break;
			}
		}
		if (target != null) {
			this.paramList.remove(target);
		}
		add(name, value);
	}

	/**
	 * フォームに項目を追加.
	 * @param nameValue
	 */
	public void add(NameValuePair nameValue) {
		this.paramList.add(nameValue);
	}

	/**
	 * フォームに項目を追加.
	 * @param name
	 * @param value
	 */
	public void add(String name, String value) {
		add(new BasicNameValuePair(name, value));
	}

	/**
	 * @return URI
	 * @throws URISyntaxException
	 */
	public URI getURI() throws URISyntaxException {
		return new URI(this.form.getAction());
	}

	/**
	 * @return the form
	 */
	public HTMLFormElement getForm() {
		return this.form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(HTMLFormElement form) {
		this.form = form;
	}

	@Override
	public Iterator<NameValuePair> iterator() {
		return this.paramList.iterator();
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder();

		buff.append("[");
		buff.append(this.form.getAction());
		buff.append("]");
		for (NameValuePair nameValue : this.paramList) {
			buff.append('\t');
			buff.append(nameValue);
			buff.append('\n');
		}
		return buff.toString();
	}
}
