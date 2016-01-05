package org.dahotre.web.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.userstore.PublicUserInfo;
import com.evernote.thrift.TException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Locale;

/**
 * Add beans and Interceptors here
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.dahotre"})
public class WebConfig extends WebMvcConfigurerAdapter {

  private static final Logger LOG = LoggerFactory.getLogger(WebConfig.class);
  @Value("#{systemEnvironment['EVERNOTE_KEY']}")
  private String evernoteKey;
  @Value("#{systemEnvironment['EVERNOTE_SECRET']}")
  private String evernoteSecret;
  @Value("#{systemEnvironment['EVERNOTE_HOST']}")
  private String evernoteHost;
  @Value("#{systemEnvironment['EVERNOTE_TOKEN']}")
  private String evernoteToken;
  @Value("#{systemEnvironment['EVERNOTE_USERNAME']}")
  private String evernoteUsername;
  @Value("#{systemEnvironment['AWS_ACCESS']}")
  private String amazonAWSAccessKey;
  @Value("#{systemEnvironment['AWS_SECRET']}")
  private String amazonAWSSecretKey;

  @Bean
  public AWSCredentials amazonAWSCredentials() {
    return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
  }

  @Bean(name = "s3")
  public AmazonS3 s3() {
    ClientConfiguration clientConfiguration = new ClientConfiguration();
    clientConfiguration.setUseGzip(true);
    AmazonS3Client client = new AmazonS3Client(amazonAWSCredentials(), clientConfiguration);
    client.setRegion(Region.getRegion(Regions.US_EAST_1));
    return client;
  }
  @Bean
  public NoteStoreClient noteStoreClient() throws TException, EDAMUserException, EDAMSystemException {
      return clientFactory().createNoteStoreClient();
  }

  @Bean
  public ClientFactory clientFactory() {
    EvernoteService evernoteService = StringUtils.equalsIgnoreCase(evernoteHost, EvernoteService.PRODUCTION.toString()) ? EvernoteService.PRODUCTION : EvernoteService.SANDBOX;
    return new ClientFactory(new EvernoteAuth(evernoteService, evernoteToken));
  }

  @Bean
  public PublicUserInfo publicUserInfo() throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
    final UserStoreClient userStoreClient = clientFactory().createUserStoreClient();
    return userStoreClient.getPublicUserInfo(evernoteUsername);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lang");
    registry.addInterceptor(localeChangeInterceptor);
    registry.addInterceptor(requestInterceptor());
  }

  @Bean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
    return cookieLocaleResolver;
  }

  @Bean
  public InternalResourceViewResolver templateResolver() {
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setPrefix("/WEB-INF/views/");
    resolver.setSuffix(".jsp");
    return resolver;
  }

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    // if true, the key of the message will be displayed if the key is not
    // found, instead of throwing a NoSuchMessageException
    messageSource.setUseCodeAsDefaultMessage(true);
    messageSource.setDefaultEncoding("UTF-8");
    // # -1 : never reload, 0 always reload
    messageSource.setCacheSeconds(0);
    return messageSource;
  }

  @Bean
  public RequestInterceptor requestInterceptor() {
    return new RequestInterceptor();
  }
}
