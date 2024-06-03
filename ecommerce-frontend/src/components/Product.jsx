
import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { Container , Paper} from '@mui/material';
import {useState, useEffect} from 'react';
import Button from '@mui/material/Button';

export default function Product() {


    const paperStyle = {padding:'50px 20px', width:600, margin:'20px auto'};
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [price, setPrice] = useState();
    const [quantity, setQuantity] = useState();
    const [products, setProducts] = useState([]);
    

    const handleClick = (e) =>{
        e.preventDefault();
        const product={name,description,price,quantity};
        console.log(product);
        fetch("http://localhost:8080/api/products/",
        {method:"POST",
        headers:{"Content-Type":"application/json"},
        body:JSON.stringify(product)})
        .then(() => {
            console.log("Product added")
        });
    }

    useEffect(() => {
        fetch("http://localhost:8080/api/products/",{
            method:"GET"})
            .then(res => res.json())
            .then((result) => {
                setProducts(result);
            }) 
    },[])




  return (
    <Container>
        <Paper elevation={2} style={paperStyle}>
            <h1>Add Product</h1>
            <Box
            component="form" sx={{'& > :not(style)': { m: 1, width: '25ch' },
            }} noValidate autoComplete="off" >

                <TextField id="outlined-basic" label="Name" variant="outlined" fullWidth
                value={name} onChange={(e) => setName(e.target.value)}/>

                <TextField id="outlined-basic" label="Description" variant="outlined" fullWidth
                value={description} onChange={(e) => setDescription(e.target.value)}/>

                <TextField id="outlined-basic" label="Price" variant="outlined" fullWidth
                value={price} onChange={(e) => setPrice(e.target.value)}/>

                <TextField id="outlined-basic" label="Quantity" variant="outlined" fullWidth
                value={quantity} onChange={(e) => setQuantity(e.target.value)}/>

                <Button variant="contained" onClick={handleClick}>Submit</Button>
            </Box>
        </Paper>

        <h1>Products:</h1>
        <Paper elevation={3} style={paperStyle}>
            {products.map(product =>(
                <Paper elevation={6} style={{margin:"10px",padding:"15px", textAlign:"left"}} key={product.id}>
                    Id: {product.id}<br/>
                    Name: {product.name}<br/>
                    Description: {product.description}<br/>
                    Price: {product.price}<br/>
                    Quantity: {product.quantity}<br/>
                </Paper>

            ))}
            </Paper>
    </Container>
    
  );
}
