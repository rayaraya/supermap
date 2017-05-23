# supermap
Web application for small-scale traffic modeling.

How to deploy it:

1. To build project you should learn about Heroku https://www.heroku.com/ and Maven https://maven.apache.org/.
2. Load this project from github and use the following command "mvn clean compile assembly:single".
3. Next you should have a heroku app (you should use url of this app in your gui), and run "heroku deploy:jar target/supermap-1.0-SNAPSHOT-jar-with-dependencies.jar --app serene-plains-38004" (see https://devcenter.heroku.com/articles/deploying-executable-jar-files).
4. For deploying the second part of the project (web gui) see https://github.com/rayaraya/supmapnjs.
