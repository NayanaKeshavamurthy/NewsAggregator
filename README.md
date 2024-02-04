# News Aggregator Mobile App


## Overview

This Android app serves as a news aggregator, allowing users to view current news articles from various sources across different categories. The app utilizes the NewsAPI.org service to fetch news sources and articles.

## Features

- Display news articles from a wide variety of sources covering various news categories.
- Use of Drawer Layout, View Binding, ViewPager2, Adapters, Internet, APIs, Android Volley, Dynamic Menus, and Implied Intents.
- Filter news sources by topic, enabling users to view relevant news articles.
- Swipe right to read the next article and left to go back to the previous article.
- Access complete extended articles on the news source's website by clicking on the article title, text, or image content.
- Professional-looking launcher icon.
- Integration with NewsAPI.org for news source and article data.
- Use of Android Volley for API JSON downloads.
- Utilization of Android View Binding in all activities.
- Dynamic menu behavior based on the retrieved data.

## API Key

You must obtain an API key from NewsAPI.org to access their services. Register at [NewsAPI.org](https://newsapi.org/register) to get your API key. Ensure the API key is supplied with NewsAPI.org queries.

## Application Behavior Diagrams

### 1. Startup

- Open the Drawer and select a news source.
- Select news topics using the options menu.
- Display news-related background before loading articles.

### 2. Selecting a Topic

- Dynamic content in the options menu based on data retrieved from news sources.
- Reduce the content of the News Sources drawer-list by selecting a topic.

### 3. Selecting a News Source

- Display the selected news source in the Navigation Bar.
- Use ViewPager2 for swipe navigation between articles.
- Display article count at the bottom of the screen.

### 4. Swipe Right (or Left) to Scroll Through Articles

### 5. Click on Article Title, Image, or Text

- Go to the extended article on the news source's website.

## News Source Data Representation

- Full news source list stored unchanged from the API.
- "Current" list of sources used to display in the drawer.
- "Current" source list updates based on topic selections.
- Convert news article date/time stamps to a user-friendly format.
- Save and restore app state during orientation changes.
- Color code news topics in the options menu and news sources in the drawer.

## Usage

1. Clone the repository.
2. Open the project in Android Studio.
3. Obtain an API key from NewsAPI.org and replace `YOUR_API_KEY` in the code with your key.
4. Build and run the app on an Android emulator or device.


