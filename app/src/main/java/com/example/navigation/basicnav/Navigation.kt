package com.example.navigation.basicnav

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink

@Composable
fun Navigation() {
    // our instance of the navController
    val navController = rememberNavController()

    // in navigation with compose we don't really need a graph but routes which we define ourselves
    // works kind of like urls in a page
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        //how our different routes look
        // if a composable with this route, display this
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }

        //route = Screen.DetailScreen.route + "/{name}/{age}" and more, both this are mandatory arguments this way
        composable(
            // like this then is optional
            //route = Screen.DetailScreen.route + "?name={name}", // we append the argument we expect
            route = Screen.DetailScreen.route + "/{name}", // we append the argument we expect
            arguments = listOf( // we create our list of arguments
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = "Test"
                    nullable = true
                }
            )
        ) { entry ->
            DetailScreen(name = entry.arguments?.getString("name")) // we navigate to it with our arguments attached
        }

        // in terminal run
        //adb shell am start -a android.intent.action.VIEW -c android.intent.category.BROWSABLE -d 'https://pl-coding.com/12666'

        //https://www.youtube.com/watch?v=z6VlP0o_sDc&list=PLQkwcJG4YTCSpJ2NLhDTHhi6XBNfk9WiC&index=34

        // we have explicit deepLinks that fire when we launch an action internally on our app
        // and the implicit ones which are launched for example from chrome etc
        // implicit deeplinks need an intent filter in our manifest in order to tell the os that
        // our app is able to open that
        composable(
            route = Screen.DetailScreenDeepLink.route,
            deepLinks = listOf( // we attach the list of deepLinks that can react to this screen
                navDeepLink {
                    uriPattern =
                        "https://pl-coding.com/{id}" // this means that when this url gets triggered our app can react to it
                    // this also reacts to intents from the browser and abd server
                    // also contains the arguments
                    action = Intent.ACTION_VIEW
                }
            ), arguments = listOf( // which arguments should be provided to this route
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { entry ->
            DetailScreenDeepLink(id = entry.arguments?.getInt("id"))
        }
    }
}

// for simplicity we use the same file, normally we wouldn't

@Composable
fun MainScreen(navController: NavController) {
    var textValue by remember {
        mutableStateOf("")
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    ) {
        TextField(value = textValue, onValueChange = {
            textValue = it
        }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))
        // in here we define how we navigate to the screen and attach the argument
        Button(
            onClick = {
                navController.navigate(Screen.DetailScreen.withArgs(textValue))
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "To detail screen")
        }

        Button(
            onClick = {
                navController.navigate(Screen.DetailScreenDeepLink.route)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "To detail (works with deepLink)")
        }
    }
}

@Composable
fun DetailScreen(name: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Hello $name")
    }
}

@Composable
fun DetailScreenDeepLink(id: Int?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "The id is $id")
    }
}