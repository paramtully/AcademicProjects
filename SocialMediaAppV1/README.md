# My Personal Project

## Project Proposal

This project is a social media application. It allows a user to interact with other users, either
through their public profile, containing posts, and information about themselves, or by directly
messaging other users. Due to the nature of this project, it will be a self-contained application,
meaning everything is stored locally on the application, and everyone will be using the 
same device to communicate with one another. Therefore, the immediate use of this application is 
quite niche. However, it provides the groundwork for an application that could later be used to make
a fully functioning, online social media platform that could be used by anyone.

I took interest in this project because of how common the use of social media platforms are. This
project gives me the opportunity to see how one would implement such an application.

User Stories:
- As a user, I want to be able to add my information about myself such as my name, age, and sex 
- As a user, I want to be able to add hobbies to my list of hobbies
- As a user, I want to be able to add posts to the list of posts on my profile
- As a user, I want to be able to change my personal biography on my profile
- As a user, I want to be able to add and remove people from my friends list
- As a user, I want to be able to send messageThread to other people
- As a user, I want to be able to search for other people

- As a user, I want to have the option to save everything in the social media application to a file
- As a user, I want to have the option to load that instance of the application from a file

## Phase 4 Task 2

Mon Nov 22 22:58:18 PST 2021
Param created a new profile
Mon Nov 22 22:58:18 PST 2021
Param added a hobby to list of hobbies
Mon Nov 22 22:58:18 PST 2021
Param added a hobby to list of hobbies
Mon Nov 22 22:58:18 PST 2021
Param changed their bio
Mon Nov 22 22:58:18 PST 2021
Param logged out
Mon Nov 22 22:58:18 PST 2021
Param was added to the network
Mon Nov 22 22:58:18 PST 2021
Logan created a new profile
Mon Nov 22 22:58:18 PST 2021
Logan changed their bio
Mon Nov 22 22:58:18 PST 2021
Logan and Param were added to each other's list of friends
Mon Nov 22 22:58:18 PST 2021
Logan logged out
Mon Nov 22 22:58:18 PST 2021
Logan was added to the network
Mon Nov 22 22:58:18 PST 2021
Peter created a new profile
Mon Nov 22 22:58:18 PST 2021
Peter changed their bio
Mon Nov 22 22:58:18 PST 2021
Peter and Param were added to each other's list of friends
Mon Nov 22 22:58:18 PST 2021
Peter and Logan were added to each other's list of friends
Mon Nov 22 22:58:18 PST 2021
Peter logged out
Mon Nov 22 22:58:18 PST 2021
Peter was added to the network
Mon Nov 22 22:58:23 PST 2021
Peter logged in
Mon Nov 22 22:58:32 PST 2021
Peter added post to list of posts
Mon Nov 22 22:58:38 PST 2021
Peter added post to list of posts
Mon Nov 22 22:58:46 PST 2021
Peter added post to list of posts
Mon Nov 22 22:58:48 PST 2021
Peter removed a post from list of posts

## Phase 4: Task 3
- I would increase cohesion within the person class. It currently handles managing personal info,
  logging in, hobbies, friends, and messaging others
  - I would fix these by creating classes such as LogInManager, FriendManager, etc to handle these
    then add these as fields to the person class
    - this can also decrease the number of classes that have associations on the person class which would
      reduce coupling
  - Note: Refer to UML_Design_Diagram.pdf for an idea of what this would look like
- I can increase cohesion within the profile class as well by creating a postManager class
- Both of my UI classes have repeated methods and have some functionality that I could abstract away to
  increase cohesion and reduce repetition
  - I would introduce an AppManager class to handle tasks such as processing the login, adding and 
    removing friends, making posts, etc
  - within the SocialMedia class app, I would create a printPrompt method that would take in
    a list of String prompts to reduce repetition of code
  - within the GUI class, I could again increase cohesion by making a new class for each menu.
    this would mean having a LoadMenu class, a logInMenu class, InAppMenu class, etc
    - these would call on each other's constructors to transition from one frame into the next