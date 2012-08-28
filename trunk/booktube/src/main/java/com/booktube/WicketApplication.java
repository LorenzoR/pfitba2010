package com.booktube;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.wicket.Application;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;

import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.convert.IConverter;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.booktube.pages.AddBookPage;
import com.booktube.pages.AdministrationPage;
import com.booktube.pages.BooksPage;
import com.booktube.pages.CampaignsAdministrationPage;
import com.booktube.pages.CategoryMenu;
import com.booktube.pages.EditBookPage;
import com.booktube.pages.EditWriterPage;
import com.booktube.pages.Error404Page;
import com.booktube.pages.ForgotPasswordPage;
import com.booktube.pages.HomePage;
import com.booktube.pages.MessagesAdministrationPage;
import com.booktube.pages.RegisterPage;
import com.booktube.pages.RegistrationConfirmationPage;
import com.booktube.pages.ReportPage;
import com.booktube.pages.ReportsAdministrationPage;
import com.booktube.pages.ShowBookPage;
import com.booktube.pages.ShowUserPage;
import com.booktube.pages.UsersAdministrationPage;
import com.booktube.pages.WorksAdministrationPage;
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

	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String DATE_FORMAT_ES = "dd/mm/aaaa";

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
		mountPage("showUser", ShowUserPage.class);
		mountPage("categoryMenu", CategoryMenu.class);
		mountPage("editUser", EditWriterPage.class);
		mountPage("confirmation", RegistrationConfirmationPage.class);
		mountPage("forgot", ForgotPasswordPage.class);
		mountPage("error404", Error404Page.class);
		mountPage("administration", AdministrationPage.class);
		mountPage("campaignsAdministration", CampaignsAdministrationPage.class);
		mountPage("messagesAdministration", MessagesAdministrationPage.class);
		mountPage("worksAdministration", WorksAdministrationPage.class);
		mountPage("usersAdministration", UsersAdministrationPage.class);
		mountPage("reportsAdministration", ReportsAdministrationPage.class);
		mountPage("reports", ReportPage.class);
		// mount(new QueryStringUrlCodingStrategy("booktube", HomePage.class));
		// mount(new QueryStringUrlCodingStrategy("writers",
		// WritersPage.class));
		// mount(new QueryStringUrlCodingStrategy("editBook",
		// EditBookPage.class));
		// mount(new QueryStringUrlCodingStrategy("addBook",
		// AddBookPage.class));
		// mount(new QueryStringUrlCodingStrategy("register",
		// RegisterPage.class));
		// mount(new QueryStringUrlCodingStrategy("showBook",
		// ShowBookPage.class));
		// mount(new IndexedParamUrlCodingStrategy("editBook",
		// EditBookPage.class));
		// mount(new IndexedHybridUrlCodingStrategy("editBook",
		// EditBookPage.class));
		// mount(new HybridUrlCodingStrategy("editBook", EditBookPage.class));
		ServletContext servletContext = super.getServletContext();
		applicationContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);

		/*
		 * org.apache.wicket.util.lang.Objects .setObjectStreamFactory(new
		 * IObjectStreamFactory.DefaultObjectStreamFactory());
		 * 
		 * Objects.setObjectStreamFactory(new WicketObjectStreamFactory());
		 */

		((ConverterLocator) getConverterLocator()).set(Date.class,
				new IConverter<Date>() {
					private static final long serialVersionUID = 1L;
					private final DateFormat mFormat = new SimpleDateFormat(DATE_FORMAT);

					public Date convertToObject(String value, Locale locale) {
						try {
							return mFormat.parse(value);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						return null;
					}

					public String convertToString(Date value, Locale locale) {
						return mFormat.format(value);
					}

				});

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
	
	@Override
	public RuntimeConfigurationType getConfigurationType() {
		return RuntimeConfigurationType.DEPLOYMENT;
	}

}
