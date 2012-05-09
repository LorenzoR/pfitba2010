package com.booktube;

import javax.servlet.ServletContext;

import org.apache.wicket.Application;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;

import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.booktube.pages.AddBookPage;
import com.booktube.pages.BooksPage;
import com.booktube.pages.EditBookPage;
import com.booktube.pages.HomePage;
import com.booktube.pages.RegisterPage;
import com.booktube.pages.ShowBookPage;
import com.booktube.pages.WritersPage;
import com.booktube.service.BookService;
import com.booktube.service.UserService;


/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see com.mycompany.Start#main(String[])
 */
public class WicketApplication extends WebApplication {

	// private BookService bookService = new BookServiceImpl();

	/*
	 * @SpringBean static UserService userService;
	 */

	// public static final SessionFactory SESSION_FACTORY = new Configuration()
	// .configure().buildSessionFactory();

	protected void init() {

		super.init();

		if (springComponentInjector == null) {
			this.springComponentInjector = new SpringComponentInjector(this);
		}
		getComponentInstantiationListeners().add(springComponentInjector);

		getMarkupSettings().setStripWicketTags(true);
		// mountBookmarkablePage("books", BooksPage.class);
		mountPage("books", BooksPage.class);
		mountPage("booktube", HomePage.class);
		mountPage("writers", WritersPage.class);
		mountPage("editBook", EditBookPage.class);
		mountPage("addBook", AddBookPage.class);
		mountPage("register", RegisterPage.class);
		mountPage("showBook", ShowBookPage.class);
//		mount(new QueryStringUrlCodingStrategy("booktube", HomePage.class));
//		mount(new QueryStringUrlCodingStrategy("writers", WritersPage.class));
//		mount(new QueryStringUrlCodingStrategy("editBook", EditBookPage.class));
//		mount(new QueryStringUrlCodingStrategy("addBook", AddBookPage.class));
//		mount(new QueryStringUrlCodingStrategy("register", RegisterPage.class));
		//mount(new QueryStringUrlCodingStrategy("showBook", ShowBookPage.class));
		// mount(new IndexedParamUrlCodingStrategy("editBook",
		// EditBookPage.class));
		// mount(new IndexedHybridUrlCodingStrategy("editBook",
		// EditBookPage.class));
		// mount(new HybridUrlCodingStrategy("editBook", EditBookPage.class));
		ServletContext servletContext = super.getServletContext();
		applicationContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);

		/*org.apache.wicket.util.lang.Objects
				.setObjectStreamFactory(new IObjectStreamFactory.DefaultObjectStreamFactory());
		
		Objects.setObjectStreamFactory(new WicketObjectStreamFactory());
		*/


	}

	/**
	 * Constructor
	 */
	public WicketApplication() {
		super();
	}

	private SpringComponentInjector springComponentInjector;

	public SpringComponentInjector getSpringComponentInjector() {
		return springComponentInjector;
	}

	public void setSpringComponentInjector(
			SpringComponentInjector springComponentInjector) {
		this.springComponentInjector = springComponentInjector;
	}

	public static WicketApplication getInstance() {
		return ((WicketApplication) WebApplication.get());
	}

	private ApplicationContext applicationContext;

	public Object getBean(String name) {
		if (name == null)
			return null;

		return applicationContext.getBean(name);
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	public Class<HomePage> getHomePage() {
		return HomePage.class;
	}

	public static WicketApplication instance() {
		return ((WicketApplication) Application.get());
	}

	public BookService getBookService() {
		// return bookService;
		return null;
	}

	public static UserService getUserService() {
		// return userService;
		return null;
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new WiaSession(request);
	}

}
