package to.kit.net.struct;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 * ドキュメント情報.
 * @author Hidetaka Sasai
 */
public final class DocumentInfo {
	private final Document document;

	private List<HTMLElement> getInputElement(Node parent) {
		List<HTMLElement> list = new ArrayList<>();
		NodeList children = parent.getChildNodes();

		for (int ix = 0; ix < children.getLength(); ix++) {
			Node node = children.item(ix);

			if (node.hasChildNodes()) {
				list.addAll(getInputElement(node));
			}
			if (node instanceof HTMLInputElement || node instanceof HTMLButtonElement) {
				list.add((HTMLElement) node);
			}
		}
		return list;
	}

	/**
	 * フォームを取得.
	 * @return フォーム情報
	 */
	public FormInfo getForm() {
		FormInfo result = new FormInfo();
		NodeList formList = this.document.getElementsByTagName("form");

		if (0 < formList.getLength()) {
			HTMLFormElement form = (HTMLFormElement) formList.item(0);

			result.setForm(form);
			for (HTMLElement element : getInputElement(form)) {
				if (element instanceof HTMLInputElement) {
					HTMLInputElement input = (HTMLInputElement) element;

					result.add(input.getName(), input.getValue());
				} else if (element instanceof HTMLButtonElement) {
					HTMLButtonElement input = (HTMLButtonElement) element;

					result.add(input.getName(), input.getValue());
				}
			}
		}
		return result;
	}

	/**
	 * リンクを取得.
	 * @param keyword
	 * @return リンク情報
	 */
	public String getLink(String keyword) {
		String result = null;
		NodeList anchorList = this.document.getElementsByTagName("a");

		for (int cnt = 0; cnt < anchorList.getLength(); cnt++) {
			HTMLAnchorElement anchor = (HTMLAnchorElement) anchorList.item(cnt);
			String href = anchor.getHref();
			String text = anchor.getTextContent();

			if (StringUtils.containsIgnoreCase(href, keyword) || StringUtils.containsIgnoreCase(text, keyword)) {
				result = href;
				break;
			}
		}
		return result;
	}

	/**
	 * @return the document
	 */
	public Document getDocument() {
		return this.document;
	}

	/**
	 * DocumentInfoインスタンス生成.
	 * @param document
	 */
	public DocumentInfo(Document document) {
		this.document = document;
	}
}
