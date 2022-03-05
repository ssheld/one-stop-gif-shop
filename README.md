# one-stop-gif-shop

One Stop GIF Shop is a web application that allows users to upload, search and download GIFs. 
Users are able to create an account which unlocks the “favorite” feature. This feature allows 
users to add GIFs currently on One Stop GIF Shop to their “favorites” page. This creates an easy 
library for users to reference when looking for GIFs they frequently use.

# Is this website currently live?

This website is not live. Currently migrating it from AWS to Heroku.

# What does your technology stack consist of?

This project currently uses Java 11 with Spring Boot. Web pages are served using Thymeleaf templating engine. Auth0 is used for authentication and authorization. AWS EC2 is utilized for hosting and load balancing of the application. AWS rekognition is used to generate keyword metadata for GIFs. AWS RDS is used to store GIF metadata and AWS S3 is used for storing actual GIFs.

# What's next?

Lots! There any many things to do. My primary focus is on back end functionality first and then cleaning up the front end (I apologize about the current mobile experience!). Adding features to user accounts and getting the GIF search engine working are next on my to-do list.
