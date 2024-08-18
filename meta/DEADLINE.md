# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

> Please provide a friendly description of your app, including
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

GitHub URL: https://github.com/vigneshkm1225/cs1302-api-app

This app asks the user to input a current NBA player's name into the two textboxes. First
name goes into the first textbox, and last name goes into the second textbox. Once the user does this,
they can click the Get Weather button, and the app will show basic weather data of the city that NBA player
plays in. For example, Lebron James plays for the Los Angeles Lakers. If the user were to input
Lebron James, then the app will output the weather of Los Angeles.


## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1

```
https://api.balldontlie.io/v1/players?first_name=lebron&last_name=james
```
The API website only allows us to generate JSON by using the following command in the terminal:

```
curl "https://api.balldontlie.io/v1/players?first_name=lebron&last_name=james" -H "Authorization: 5a9c49dc-b88b-4fd1-b59c-4ba247934272"
```
When I made my http request in my app, I used the .setHeader() method to use my API key to make
the request.

The rate limit for this API is 30 req/minute.

### API 2

```
https://api.weatherbit.io/v2.0/current?city=atlanta&key=210e3c9cb6574ad9bc2cd999fe39b04d&include=hourly&units=I
```
I am using this API on a free trial, which expires on 5/23/24. The rate limit for this API is 1500 req/day.

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

By working on this project, I learned that I am capable of creating really powerful and useful apps. APIs
are really great tools for programmers since a programmer can use it to create something very unique and useful
to us. I am really looking forward to future projects I can do using APIs.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

If I could restart my project, I would try to pick an idea that is more practical for everyday use. At first I
sort of assumed that free APIs were only useful for simple passion projects or gimmicks. After looking around
more, I now know that even free APIs can be used to create everyday apps.
