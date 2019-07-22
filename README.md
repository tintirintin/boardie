# Boardie 0.9 - simple networking app

This is a demo of REST API project of the code challenge trial for HSBC recruitment process, 
brought to you by Miroslaw Sniezek :muscle:

A format of accepting GET/POST requests is JSON, so you can use e.g. 
Postman (https://www.getpostman.com/)
or CURL (https://curl.haxx.se/download.html) to test the app.

The application has few functions, all of them are described below. Happy using!

### Create the message

**POST /rest/message**

User has the possibility to write a message.

**Request**
```
{
	"login": "<user_login>",
	"content": "<user_message>"
}
```
e.g.
```
{
	"login": "David",
	"content": "Siemanko"
}
```
**Responses**

`HTTP 201` - created, whole post with ID as a response

`HTTP 400` - bad request, in case of empty login or message

### Follow an user

**POST /rest/follow**

User has the possibility to follow another user. 

**Request**
```
{
	"login": "<user_login>",
	"content": "<user_you_want_to_follow>"
}
```
e.g.
```
{
	"login": "David",
	"content": "Johny"
}
```
**Responses**

`HTTP 202` - accepted, whole user data as a response

`HTTP 400` - bad request, in case of empty login or following user

### Get the wall with all messages

**GET /rest/wall**

User has the possibility to see the messages posted by all users.

**Request**

No parameters.

**Responses**

`HTTP 200` - OK, list of messages as a response, can be empty

### Get the wall of the user

**GET /rest/userwall/{user}**

User has the possibility to see the messages of one user (e.g. himself). 

**Request**
```
parameter |  type  |  description
----------------------------------
user      | string |  user login 
```
e.g.
```
GET /rest/userwall/Johny
```
**Responses**

`HTTP 200` - OK, list of messages as a response

`HTTP 400` - bad request, in case of user not found

### Get the timeline of following users

**GET /rest/timeline/{user}**

User has the possibility to see the messages of all users he follow  

**Request**
```
parameter |  type  |  description
----------------------------------
user      | string |  user login 
```
e.g.
```
GET /rest/timeline/Johny
```
**Responses**

`HTTP 200` - OK, list of messages as a response, can be empty

`HTTP 400` - bad request, in case of user not found

### Get the one post by ID

**GET /rest/post/{id}**

User has the possibility to see the chosen message by ID 

**Request**
```
parameter |  type  |  description
----------------------------------
id        | long   |  ID of the post 
```
e.g.
```
GET /rest/post/5
```
**Responses**

`HTTP 200` - OK, post as a response

`HTTP 400` - bad request, in case of post not found

