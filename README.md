SpringMVCStarter
================

A starter skeleton for Spring MVC

### Has-a
1. Basic spring mvc web initialization without Spring data
2. Logging with slf4j
3. Common sense CSS JS libs from CDN. Typeform for feedback.
4. debug cookie can be set by query param debug=true
5. Authorization of controller actions via @Authable annotation
6. Heroku-ready with Procfile, deploy plugin and or git push heroku master

### How to use
1. If you want to suggest changes, then fork and send a pull request. Or just add an issue.
2. If you want to get started with your Spring MVC app using this skeleton, then DO NOT git clone. Instead perform git archive as suggested in http://stackoverflow.com/questions/160608/do-a-git-export-like-svn-export

### What do I do after downloading?
1. Ensure you have Java 1.8 and Maven 3.2.3 installed.
2. `mvn clean install -U` in the app dir.
3. If you are planning to use Heroku, then download the Heroku toolbelt. This will also download `foreman`.
4. Deploy.
  1. One way to deploy the application locally is to run `foreman start web`
  2. `java $JAVA_OPTS -jar target/dependency/jetty-runner.jar --port $PORT target/*.war` from the app dir. This is what foreman would do too.
  3. Another way is to mvn tomcat7:run. This can be done from IntelliJ CE from the Plugins section of the 'Maven projects' pane.
5. 4.1 and 4.2 will host the app on localhost:5000, whereas tomcat7 will host on localhost:8080, by default. Confirm that a simple HTML page is displayed and has a title tag in head with Company.com as a value.

### Initialize git
1. `git init`
2. `git add . ` The default `.gitignore` will take care of some common default files.
3. `git commit -m "initial commit"`
4. If you wish to set up a github or bitbucket repo, go to their respective web consoles and create a repo. It will give you a `.git` URL such as `https://github.com/dahotre/SpringMVCStarter.git`
5. `git remote add origin URL_FROM_ABOVE.git`
6. `git push -u origin master`

### Deploying to Heroku
1. `heroku create`
2. Edit your `pom.xml` to add the generated (or custom) app name in the `<profile>` section. I.e., replace `immense-plateau-6256` with your newly generated app name.
3. Deploy
  1. First way to deploy to heroku is by using a git remote. `git push heroku master` . This will push your local code as well as execute a maven compile and deploy the generated war.
  2. Second way : assuming that you had performed a `mvn clean install` or `mvn package` locally, you will have a `*.war` file ready to be deployed. Using the included heroku maven plugin, execute `mvn heroku:deploy-war -P test`. 
4. If either of the above 2 steps work out without issue, then `heroku open` to see the website live in your default browser.
