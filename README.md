# Groupomania production website  
  
## How to run the code on your system:  
  
- Clone the repository with the following command: 'git clone https://github.com/nanoandrew4/Groupomania-proto.git'
- Change directories into the cloned repository
- Run 'mvn clean spring-boot:run', which will start the server using the spring boot maven plugin, which compiles and runs the Spring Boot application

## How to deploy to Heroku

- Log in to your Heroku account
- In the dashboard, click on New (top center-right), then create new app
- Input the desired app name, and region, depending on where your users are
- Click on 'Create app'
- For deploying the application, you have two options
    - Deploy using Heroku CLI
    - Fork the repository, and connect to your GitHub account through Heroku, select the repository and branch to deploy, and click on 'Deploy Branch'
    
### Deploying using the Heroku CLI
- [Download](https://devcenter.heroku.com/articles/heroku-command-line) the Heroku CLI if you haven't
- Log in through the CLI: "heroku login"
- If you haven't cloned the repo yet, run the following: "heroku git:clone -a {heroku_app_name}", which will clone the repo and set the heroku remote in one step
- If you have already cloned the repo, change directories into the root of the repo and run the following command: "heroku git:remote -a {heroku_app_name}", which will add the heroku remote so that the application can be deployed
- Once either of the two steps above have been completed, run "git push heroku master" to deploy the application. The build process logs will be displayed on your console, until the build process finishes

### Deploying using GitHub
- Fork the repository to your GitHub account
- Log in to your Heroku account, and navigate to the following URL (replace {heroku_app_name} with the name you gave to the app): 'https://dashboard.heroku.com/apps/{heroku_app_name}/deploy/github'
- Connect your GitHub account to Heroku
- Search for the repository, and select the branch to deploy
- Click on 'Deploy Branch'