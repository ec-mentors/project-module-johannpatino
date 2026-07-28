# Diary of developing Deliciousnessness

This would be a very informal diary to document the process of me developing this app, expect a lot of gramitcal errors aswell as typos and mistakes, no judgement pls. maybe this would just be mostly questions or doubts that come up while building the project or even include cool stuff or just praise myself

---

the whole idea for this project is to organize the complete disaster of recipes Ela has, i wanted a centralized page with all the recipes she enjoys, or even plans to cook in the future, since she speaks all the languages ever, theres recipes in different languages, ingredients in different languages. she wants a sorting system that organizes recipes through different categories, and also be able to sort them through ingredients, here's the tricky part (or what i believe the tricky part is), sorting ingredients in the way that if you search for an ingredient in one language you get the result regardless of the language. 

### **27/07**

started the project wirh start.spring.io, knowing which dependencies one needs it's still something i need to learn, I guess with time I will know or whenever I hit a roadblock in the future I will know then.

using ai, specifically claude code opus 5 (balling out), to create a roadmap and plan what the steps would be for programming everything from start to finish (might include this in build_plan). 


### **28/07**

modified the application.properties to include the instructions on how to connect to the database

created an enum of seasons to pass it later to the recipe data class

created the data class recipe, with the params I have on the schema. 

```
@Entity
```
This is, for my understanding, the most important annotation, it tells hibernate that this class maps to a table in the database. And with the setting in `application.properties`: 

```
spring.jpa.hibernate.ddl-auto=update
```

meaning: creates missing tables and adds missing columns. (does NOT change if later there's a change in a data type or a name, it simply just creates and adds)

created the `RecipeRepository`, pretty simple, it's just an interface extending to `JpaRepository` so i get all the methods already included there. if there's a need for a custom method this is where i would add it first. 

created the `RecipeService`, basic service class, here I get a Repository object and call on it for the methods and implement them.

created the `RecipeController`, this is basically what would control what gets sent to the browser and how, learned a new thing called `ResponseEntity`, this is a Java object representing all of a HTTP response (status code, header, body).

tested everything in postman. made a collection called Deliciousnessness so all the requests are saved in one place instead of me retyping the url every time, apparently this is also a good thing to just open and run during a sprint review.

sent the POST first with a carbonara recipe. got 201 Created back, and the response had two fields i never sent: `id` and `createdAt`. the id comes from the database, createdAt comes from the `@CreationTimestamp` annotation, so the server filled both in by itself. first time the whole stack actually worked end to end, felt pretty good.

then the rest: GET all, GET by id, PUT, DELETE.

GET all(called READ on postman) returns a json array with square brackets instead of curly ones, and if there's nothing in the table you get `[]` with a 200, not a 404. 404 would mean the endpoint doesnt exist. the endpoint exists fine, the list is just empty. "no results" is a successful answer.

DELETE gives back 204 No Content literally means "it worked and im deliberately sending you nothing back", which is right, the thing is gone, what would it even return. sent the same delete twice and the second one gave 404, that's the existsById check doing its job.

**something I realized today**

the ids keep incrementing and skip numbers when i delete stuff. thought this was a bug.

it's not. the id comes from a sequence, which is a counter postgres keeps completely separate from the table. it hands out numbers and never takes them back. deleting a row doesnt tell the sequence anything, it doesnt even know the table exists. failed inserts eat numbers too. the reason is concurrency, if the counter rolled back on every failed transaction then inserts would have to queue up waiting on each other and it would be slow.
