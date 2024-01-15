package com.cc221012_cc221016.stash.ui.views

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.cc221012_cc221016.stash.models.MainViewModel
import com.cc221012_cc221016.stash.ui.views.Composables.LoginRegisterView

sealed class Screen(val route: String){
    object First: Screen("first")
    object Second: Screen("second")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(mainViewModel: MainViewModel){
    // You can replace this with the actual value from your ViewModel or state
    val hasUser = false

    LoginRegisterView(hasUser)
}


























//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainView(mainViewModel: MainViewModel){
//    val state = mainViewModel.mainViewState.collectAsState()
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = {BottomNavigationBar(navController, state.value.selectedScreen)}
//    ) {
//        NavHost(
//            navController = navController,
//            modifier = Modifier.padding(it),
//            startDestination = Screen.First.route
//        ){
//            composable(Screen.First.route){
//                mainViewModel.selectScreen(Screen.First)
//                mainScreen(mainViewModel)
//            }
//            composable(Screen.Second.route){
//                mainViewModel.selectScreen(Screen.Second)
//                mainViewModel.getStudents()
//                displayStudents(mainViewModel)
//            }
//        }
//    }
//}

//@Composable
//fun BottomNavigationBar(navController: NavHostController, selectedScreen: Screen){
//    BottomNavigation (backgroundColor = MaterialTheme.colorScheme.primary) {
//        NavigationBarItem(
//            selected = (selectedScreen == Screen.First),
//            onClick = { navController.navigate(Screen.First.route) },
//            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "") })
//
//        NavigationBarItem(
//            selected = (selectedScreen == Screen.Second),
//            onClick = { navController.navigate(Screen.Second.route) },
//            icon = { Icon(imageVector = Icons.Default.AccountBox, contentDescription = "") })
//
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun mainScreen(mainViewModel: MainViewModel){
//    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
//    var uid by remember { mutableStateOf(TextFieldValue("")) }
//
//    Column (
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState()),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "ROOM Demo", fontSize = 50.sp,  style = TextStyle(fontFamily = FontFamily.Cursive))
//        Image(
//            painter = painterResource(id = R.drawable.ic_launcher_foreground),
//            contentDescription = "Box"
//        )
//        Spacer(modifier = Modifier.height(50.dp))
//
//        TextField(
//            value = name,
//            onValueChange = {
//                    newText -> name = newText
//            },
//            label = { Text(text = "Name" ) }
//        )
//
//        TextField(
//            modifier = Modifier.padding(top = 20.dp),
//            value = uid,
//            onValueChange = {
//                    newText -> uid = newText
//            },
//            label = { Text(text = "UID") }
//        )
//
//        Button(
//            onClick = { mainViewModel.save(BccStudent(name.text,uid.text)) },
//            modifier = Modifier.padding(top = 20.dp)
//        ) {
//            Text(text = "Save", fontSize = 20.sp)
//        }
//    }
//}
//
//@Composable
//fun displayStudents(mainViewModel: MainViewModel){
//    val state = mainViewModel.mainViewState.collectAsState()
//
//    LazyColumn (
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        item{ Text(text = "BCC Students:",fontWeight = FontWeight.Bold) }
//
//        items(state.value.students){
//            Row(modifier = Modifier
//                .fillMaxWidth()
//                .padding(20.dp)
//                .clickable { mainViewModel.editStudent(it) }
//            ){
//                Column (modifier = Modifier.weight(1f)) {
//                    Text(text = "Name: ${it.name}")
//                    Text(text = "UID: ${it.uid}")
//                }
//                IconButton(onClick = { mainViewModel.deleteButton(it)}) {
//                    Icon(Icons.Default.Delete,"Delete")
//                }
//            }
//        }
//    }
//    Column {
//        editStudentModal(mainViewModel)
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun editStudentModal(mainViewModel: MainViewModel){
//
//    val state = mainViewModel.mainViewState.collectAsState()
//
//
//    if(state.value.openDialog){
//        val student = mainViewModel.bccStudentState.collectAsState()
//        var name by rememberSaveable { mutableStateOf(student.value.name) }
//        var uid by rememberSaveable { mutableStateOf(student.value.uid) }
//
//        AlertDialog(
//            onDismissRequest = {
//                mainViewModel.closeDialog()
//            },
//            text = {
//                Column {
//                    TextField(
//                        modifier = Modifier.padding(top = 20.dp),
//                        value = name,
//                        onValueChange = { newText -> name = newText },
//                        label = { Text(text = "Name" ) }
//                    )
//
//                    TextField(
//                        modifier = Modifier.padding(top = 20.dp),
//                        value = uid,
//                        onValueChange = { newText -> uid = newText },
//                        label = { Text(text = "UID") }
//                    )
//                }
//            },
//            confirmButton = {
//                Button( onClick = { mainViewModel.updateStudent(BccStudent(name,uid,student.value.id)) }) {
//                    Text("Confirm")
//                }
//            }
//        )
//    }
//
//}
//
//
