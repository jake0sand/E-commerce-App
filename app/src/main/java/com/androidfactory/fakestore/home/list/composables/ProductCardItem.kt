package com.androidfactory.fakestore.home.list.composables

import android.inputmethodservice.Keyboard
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidfactory.fakestore.R
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProductCardItem(
    productTitle: String,
    productCategory: String,
    image: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        backgroundColor = Color.White,
        shape = roundedCornerShape(),
        elevation = 8.dp
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(ratio = 1f, matchHeightConstraintsFirst = true)
            ) {

                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = roundedCornerShape(),
                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_launcher_background),
//                        contentDescription = "",
//                        modifier = Modifier.fillMaxSize()
//                    )
                    GlideImage(
                        contentScale = ContentScale.Crop,
                        imageModel = image,
                        contentDescription = "",
                        previewPlaceholder = R.drawable.ic_launcher_background,
                        modifier = Modifier.fillMaxSize()
                    )
                    Row(Modifier.fillMaxWidth()) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )

                        var isFavorited by rememberSaveable { mutableStateOf(false) }
                        IconButton(onClick = { isFavorited = !isFavorited }) {
                            Icon(
                                imageVector =
                                if (!isFavorited) Icons.Default.FavoriteBorder
                                else Icons.Default.Favorite,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }


                    }
                }

                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = productTitle,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = productCategory,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.purple_500)
                )
                Divider(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    color = Color.Transparent
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "109.95",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.purple_500),
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f)
                            .align(CenterVertically)
                    )
                    Box(Modifier) {

                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .width(68.dp)
                                .height(34.dp)
                                .padding(start = 8.dp),
                            colors = buttonColors(backgroundColor = colorResource(id = R.color.purple_500)),
                            shape = roundedCornerShape()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "",
                            tint = Color.Green,
                            modifier = Modifier
                                .size(16.dp)
                                .align(CenterStart)
                                .clip(shape = CircleShape)
                                .background(Color.White)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun roundedCornerShape() = RoundedCornerShape(12.dp)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ThisCard() {
    Column(Modifier.fillMaxSize()) {
        ProductCardItem(
            modifier = Modifier.padding(16.dp),
            productTitle = "Preview Title",
            productCategory = "Men's Clothing",
            image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"
        )

    }
}

