package com.example.roomdatabase

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomdatabase.ui.theme.RoomDatabaseTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

class MainActivity : ComponentActivity() {
    var deletionsurety:MutableState<Boolean> = mutableStateOf(false)
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {

            val database=Room.databaseBuilder(applicationContext, MyDatabase::class.java,"MyDb").build()
            var userList:MutableList<MyTable> = mutableListOf()
            if(deletionsurety.value){
                deletionSurety()
            }


            GlobalScope.launch {
                var noteList=database.mydao().getData()
                userList.clear()
                userList.addAll(noteList)
            }
           // deletionSurety()
            homeScreen(database, userList)
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun homeScreen(db:MyDatabase, userList:MutableList<MyTable>){
        Column(modifier= Modifier
            .fillMaxSize()
            .background(Color.White)
            , horizontalAlignment = Alignment.CenterHorizontally) {
            var name= remember {
                mutableStateOf("")
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                , horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.list_todo), contentDescription ="todoList", modifier= Modifier
                    .clip(shape = RoundedCornerShape(50.dp))
                    .size(42.dp) )
                Spacer(modifier = Modifier.width(50.dp))
                Text(text = "ToDO Manager", fontWeight = FontWeight(500), fontSize = 23.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier
                .width(350.dp)
                .padding(10.dp)
                , horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(value = name.value, onValueChange ={name.value=it} )
                Image(painter = painterResource(id = R.drawable.add), contentDescription = "add png", modifier= Modifier
                    .clip(shape = RoundedCornerShape(50.dp))
                    .size(42.dp)
                    .clickable {

                        GlobalScope.launch {
                            db
                                .mydao()
                                .insertData(MyTable(0, name.value))
                            userList.clear()
                            var noteList = db
                                .mydao()
                                .getData()
                            userList.addAll(noteList)
                            name.value = ""
                        }
                        Toast
                            .makeText(
                                applicationContext,
                                "Note Added successfully",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    })
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                items(userList){
                    item->
                    noteItem(item.id, item.name, db, userList)
                }
            }
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    @Composable
    fun noteItem(id:Long, note:String, db: MyDatabase, userList: MutableList<MyTable>){
        var noteTitle= remember {
            mutableStateOf("${id}. $note")
        }

        Column {
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Card(modifier= Modifier
                    .width(330.dp)
                    .height(70.dp), shape = RoundedCornerShape(25.dp),
                    elevation = CardDefaults.cardElevation(10.dp)) {
                    Row(modifier= Modifier
                        .width(330.dp)
                        .height(70.dp)
                        .padding(horizontal = 15.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Absolute.SpaceBetween) {
                        Text(text = noteTitle.value, fontSize = 20.sp, fontWeight = FontWeight(400))
                        Image(painter = painterResource(id = R.drawable.delete), contentDescription ="delete icon", modifier= Modifier
                            .size(35.dp)
                            .clickable {
                               // deletionsurety.value = true
                                GlobalScope.launch(Dispatchers.IO) {
                                    db
                                        .mydao()
                                        .deleteData(id)
                                    var userData = db
                                        .mydao()
                                        .getData()
                                    userList.clear()
                                    userList.addAll(userData)
                                }
                                Toast
                                    .makeText(
                                        applicationContext,
                                        "Note Deleted successfully",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            } )
                    }
                }
            }
            Spacer(modifier = Modifier.height(13.dp))
        }
    }

    @Composable
    fun deletionSurety(){
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
              Card (modifier = Modifier
                  .width(300.dp)
                  .height(300.dp), elevation = CardDefaults.cardElevation(10.dp)){
                  Dialog(onDismissRequest = { /*TODO*/ }) {
                      Text(text = "hello ji ")
                  }
              }
        }
    }
}



