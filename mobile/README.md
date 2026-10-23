# 1. Setup. 
Supported emulators 24 - 37 sdk
online api was hosted on cybersmart so no changes to be made
However if you wish to develop locally, update android manifest to accept clear data
Update ApiClient baseUrl to local host url.

# 2. Architecture
## 2.1 Architecture
- MVVM structure used Model, views, view model.
- Model used to store the data required, such as User.
- Views, in our case it is activities that load the correct fragments and observes viewModel states.
- ViewModel, shares state and calls the backend using frontend repository.

## 2.2 Data and network layer
- AppContainer is used for dependency injection container for easy construction and later caching oppurtunity.
- ApiClient was written to work with custom backend. Gson is used to map the response. 
This is tied to ApiResponse, that makes a consistent mapping, ApiCallback and ApiError for
consistent mapping.
- Repositories calls the frontend service. Later it could cache such as save to local db.
- Services uses ApiClient and constructs the network object. 

## 2.3 Authentication and Session management
- Preference manager and secureSession used to encrypt refresh and access token safely on device.
- Splash activity act as entry point, and checking token expiration and redirecting.
- Base activity observes logged in live data and redirects, so the ApiClient updated live data

## 2.4 UI Components
- Adapters used to construct dynamic UI for lists.
- Reusable nav bar created.
