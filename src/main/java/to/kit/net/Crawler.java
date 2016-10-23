package to.kit.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.cyberneko.html.parsers.DOMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import to.kit.net.struct.DocumentInfo;
import to.kit.net.struct.FormInfo;
import to.kit.net.struct.Preference;

/**
 * Crawler.
 * @author Hidetaka Sasai
 */
public final class Crawler {
	private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

	private HttpClient client;

	private Document getDocument(HttpEntity entity) throws IOException, SAXException {
		String content;

		try (InputStream in = entity.getContent()) {
			content = IOUtils.toString(in, Charset.defaultCharset());
//			LOG.debug(content);
		}
		InputSource inputSource = new InputSource(new StringReader(content));
		DOMParser parser = new DOMParser();

		parser.parse(inputSource);
		return parser.getDocument();
	}

	/**
	 * ドキュメント取得.
	 * @param request
	 * @return ドキュメント情報
	 * @throws IOException
	 * @throws SAXException
	 */
	public DocumentInfo getDocument(final HttpUriRequest request) throws IOException, SAXException {
		Document document;

		try (CloseableHttpResponse response = (CloseableHttpResponse) this.client.execute(request)) {
			document = getDocument(response.getEntity());
		}
		return new DocumentInfo(document);
	}

	/**
	 * フォーム送信.
	 * @param formInfo
	 * @return ドキュメント情報
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws SAXException
	 */
	public DocumentInfo submit(FormInfo formInfo) throws URISyntaxException, IOException, SAXException {
		HttpPost request = new HttpPost(formInfo.getURI());
		Document document;

		LOG.debug("[submit]");
		for (NameValuePair nameValue : formInfo) {
			LOG.debug("\t" + nameValue);
		}
		request.setEntity(new UrlEncodedFormEntity(formInfo, Charset.defaultCharset()));
		try (CloseableHttpResponse response = (CloseableHttpResponse) this.client.execute(request)) {
			LOG.debug("Status:" + response.getStatusLine());
			document = getDocument(response.getEntity());
		}
		return new DocumentInfo(document);
	}

	/**
	 * Crawlerインスタンス生成.
	 */
	public Crawler() {
		Preference pref = Preference.Instance;
		List<Header> headers = new ArrayList<>();

		headers.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		headers.add(new BasicHeader("Accept-Language", "ja,en-US;q=0.7,en;q=0.3"));
		headers.add(new BasicHeader("Accept-Encoding", "gzip, deflate, br"));
		this.client = HttpClientBuilder.create()
				.setUserAgent(pref.getUserAgent())
				.setDefaultHeaders(headers).build();
	}
}
