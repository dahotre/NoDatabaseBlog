NoDatabaseBlog
==============

A Blog created using Spring MVC. Evernote is used to create/update the blog posts.

### How to use
1. If you want to suggest changes, then fork and send a pull request. Or just add an issue.

### What do I do after downloading?
1. Ensure you have Java 1.8 and Maven 3.2.3 installed.
2. `mvn clean install -U` in the app dir.
3. If you are planning to use Heroku, then download the Heroku toolbelt. This will also download `heroku local`.
4. Deploy.
  1. One way to deploy the application locally is to run `heroku local web`
  2. `java $JAVA_OPTS -jar target/dependency/jetty-runner.jar --port $PORT target/*.war` from the app dir. This is what foreman would do too.
  3. Another way is to mvn tomcat7:run. This can be done from IntelliJ CE from the Plugins section of the 'Maven projects' pane.
5. 4.1 and 4.2 will host the app on localhost:5000, whereas tomcat7 will host on localhost:8080, by default. Confirm that a simple HTML page is displayed and has a title tag in head with Company.com as a value.

### Deploying to Heroku
1. `heroku create`
2. Edit your `pom.xml` to add the generated (or custom) app name in the `<profile>` section. I.e., replace `immense-plateau-6256` with your newly generated app name.
3. Deploy
  1. First way to deploy to heroku is by using a git remote. `git push heroku master` . This will push your local code as well as execute a maven compile and deploy the generated war.
  2. Second way : assuming that you had performed a `mvn clean install` or `mvn package` locally, you will have a `*.war` file ready to be deployed. Using the included heroku maven plugin, execute `mvn heroku:deploy-war -P test`. 
4. If either of the above 2 steps work out without issue, then `heroku open` to see the website live in your default browser.
