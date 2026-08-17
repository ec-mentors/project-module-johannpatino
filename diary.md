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

### **29/07 - 30/07**

spent most of time improving and fixing the build plan claude did, since what it previously marked as a week work I did in one day.

also learned some concepts that are going to be useful, I will be writing here the concepts and what I managed to understand from them. 

- Foreign keys: 
    
    A foreign key is a column thats holds ANOTHER TABLE'S primary key value, this would be extremely useful later when adding ingredients in different languages, the ingredient itself would be added in an `ingredient` class and there would be another class maybe `ingredient_name` that has an id of the language name of the ingredients as well as the foreign key of the ingredient so they can be tied together.

- The owning side:

    Is whichever side carries `@JoinColumn` or `@JoinTable`, that's the side Hibernate reads when deciding what SQL to write, the other side declares `mappedBy`, it's Hibernate being told "this field maps to no column, ignore it when generating SQL.

- FetchType

    - Lazy: 

        Hibernate puts a proxy in the field: a stand-in object holding only the id. The SELECT for the real data doesn"t run until something calls the getter.

    - Eager:

        The data is loaded immediately alongside its parent, in the same query or a follow-up.

    The JPA default for `@ManyToOne` is `EAGER`, it is regarded as a bad default. When I go to write the RecipeIngredients I should change it to `LAZY`, otherwise everything will be loaded up every time, wheter I want to or not

- Cascade and orphanRemoval

    - Cascade - operations flow from parent to child

        Without it, JPA treats every entity independently.  Save a Recipe holding three brand-new RecipeIngredient objects and you get:

        ```
        TransientObjectException: object references an unsaved transient instance
        ```
        Hibernate saved the recipe, found references to three objects it has never seen, and refuses to guess. You'd have to save each child yourself, in the right order.

    - orphanRemoval - the child dies when it"s disowned

        This one activates when a child is removed from the parent's collection while the parent stays alive. 

        This is what I need for editing. When someone edits a recipe and drops "200 g onion" from the ingredient list, the recipe isn't going anywhere, but that ingredient line has to.

    - Transactional

        A transaction is a group of database operations that all succeed together or all fail together.

---

after a small break, I continue. 
### **03/08**


Created the `Category` entity, it has a new thing worth noting, in the `@Column` we have `unique = true`, this is important so no duplicates of category can be created.

Created the `Ingredient` entity, same way as the Category one. In it there's the `canonicalName` column, this would be the label for an ingredient on a database, and later another entity would hold all the different language names.

added both repository for category and ingredient, nothing notable here, added one method in the interface of each to find by their respective Strings.

Created the `IngredientName` Entity, this one has more stuff since it's getting the Foreign Key from Ingredient, and connecting to it, 

```
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "language_code"}))
```
- 


```
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
```

- ManyToOne : Many `IngredientName` point at one `Ingredient` one name has one ingredient, and there could be several names pointing at one ingredient.

- FetchType.LAZY: overriding the default, if no every IngredientName one loads would drag its Ingredient along in a second query. if there are several rows of IngredientName, an EAGER fetching type would mean several pointless extra `SELECT`'s to fetch a `canonicalName` that is never read.  

the data type would be the object Ingredient, here it differs from raw SQL, with Hibernate you navigate to the object and it turns that integer from Ingredient into a column in IngredientName. 


### **04/08**

created the `RecipeIngredient` entity, this one has two foreign keys, being `recipe_id` and `ingredient_id`. is the join between recipe and ingredient.it has 2 `@ManyToOne` one to `Recipe` and one to `Ingredient` first table with two foreign keys. 

created the first issue on github, with the help of ai for redaction and prepared everything for further developing this week. 



## **07/08**

added two collections to `Recipe`: 

***
@ManyToMany
@JoinTable(name = "recipe_categories",
joinColumns = @JoinColumn(name = "recipe_id"),
inverseJoinColumns = @JoinColumn(name = "category_id"))
private Set<Category> categories = new HashSet<>();
***

***
@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
***

`Set` for categories because a recipe can only be the same category once (of course, it cannot be twice soup)

`List` for ingredients because the order matters.

also added `addIngredient` and `removeIngredient` methods

now this creates a crash, the crash occurs in recipe categories, since the default for manytomany is fetchtype lazy.
hibernate doesn't even look at the categories, it just leaves an "i owe you this information
when you ask for it" in that field and moves on. then my service method finishes and the
connection closes. then spring receives the recipe and tries to turn it into json, sees the
"i owe you" and tries to cash it in, but the connection is already closed so it crashes.

it crashes every time, not just when the categories are empty. the problem is having to ask at
all, not what the answer would have been. finding out there are zero still needs a query.

this gets fixed with dtos. a dto is just a plain object holding the actual values, no i owe
yous, and hibernate has nothing to do with it. the important part is WHEN the snapshot is
taken, it has to happen while the connection is still open, otherwise it would crash in exactly
the same way. so the service copies everything into the dto inside the transaction, and that
plain object is what spring uses to make the json.

##
### **SICK WEEK**
##

## **17/08**


created the first dtos, the `RecipeIngredientDto` and the `RecipeDto` 



in the `RecipeDto` we pull the name of the categories this way 

***
  recipe.getCategories().stream()
                        .map(Category::getName)
                        .collect(Collectors.toSet())
***
this collection forces the query to run, now the json gets the names instead of an *"I owe you"* from hibernate

this also happens in the next line 

***
recipe.getRecipeIngredients().stream()
.map(RecipeIngredientDto::from)
.toList()
***

loads the ingredients and returns the list in order for Spring to be able to read them and turn them into Json.

updated the RecipeService to use now the Dto, and now every single one uses `@Transactional`, this is the actual fix for the 500 error, this keeps the door open while from() runs.

added `readOnly = true` on the find methods to tell hibernate that nothin will change there, so it skips dirty checking, this is only for very small performance gain.


the controller changes the same way, now everything is using a dto.