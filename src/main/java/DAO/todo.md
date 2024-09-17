You will need to design and create your own DAO classes from scratch. 
You should refer to prior mini-project lab examples and course material for guidance.

Please refrain from using a 'try-with-resources' block when connecting to your database. 
The ConnectionUtil provided uses a singleton, and using a try-with-resources will cause issues in the tests.

//Alright so we know our Data Access Object is going to need methods that use the objects when have created.
// The Services will act as a mapping between our DAO and Controller's, which are already created. 
// We can probably reference the SocialMediaController to see what's up


// We have to manage our user accounts so we'll need the some sort of api connection request here and then we have to persist the data to eventually perform crud operations when needed. 
// In addition to managing our users information, we will also track and manage the messages they send throughout the application

// IMPORTANT: Any user should be able to see ALL messages or only ones for particular users.
// This is where we will set our endpoints

// Need to be able to process information such as logins, registrations, message creations, message updates, and message deletions. CRUD.