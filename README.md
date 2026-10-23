# Android Project 6 - BitFit Part 2

Submitted by: **David Mack**

**BitFit** is a multi-screen health metrics app that allows users to track daily nutrition, calories, notes, and photos, featuring a dedicated Dashboard Fragment, navigation bar UI, and trend analytics.

Time spent: **6** hours spent in total

## Required Features

The following **required** functionality is completed:

- [x] **Use at least 2 Fragments** (`LogListFragment` and `DashboardFragment`)
- [x] **Create a new dashboard fragment where users can see a summary of their entered data**
- [x] **Use one of the Navigation UI Views (BottomNavigationView, Drawer Layout, Top Bar) to move between the fragments**

The following **optional** features are implemented:

- [x] **Add a more advanced UI (e.g: Graphing / Trend breakdown) for tracking trends in metrics**
- [x] **Implement daily notifications to prompt users to fill in their data**

The following **additional** features are implemented:

- [x] Photo picker integration for daily logs.
- [x] Live Room Database observation using Kotlin Coroutines Flow across fragments.
- [x] Custom Empty State UI.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

blob:https://ezgif.com/c0c7ee46-be61-446f-825c-091630c15773

GIF created with Kap / ScreenToGif.

## Notes

Navigating between fragments with BottomNavigationView while maintaining live Flow observations from Room DB was implemented smoothly using `viewLifecycleOwner`.

## License

    Copyright 2026 David Mack

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
