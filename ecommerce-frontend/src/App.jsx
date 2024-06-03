
import './App.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import ListProducts from './components/ListProducts.jsx'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import ProductComponent from './components/admin/ProductComponent.jsx'
import HomePage from './components/Homepage/Homepage.jsx'
import ProductDetailPage from './components/Product/ProductDetailPage.jsx'
import CartPage from './components/Product/CartPage.jsx'
import AddedToCartPage from './components/Product/AddedToCartPage.jsx'
import SearchResults from './components/Homepage/SearchResults.jsx'
import OrdersPage from './components/user/OrdersPage.jsx'
import ReviewPage from './components/Product/ReviewPage.jsx'
import UserProfile from './components/admin/UserProfile.jsx'
import AddAddressForm from './components/user/AddAddressForm.jsx'
import UpdateAddressForm from './components/user/UpdateAddressForm.jsx'
import AddressComponent from './components/user/AddressComponent.jsx'
import ChangePasswordComponent from './components/user/ChangePasswordComponent.jsx'
import UpdateUserComponent from './components/user/UpdateUserComponent.jsx'
import AdminListProducts from './components/admin/AdminListProducts.jsx'
import AdminListUsers from './components/admin/AdminListUsers.jsx'
import UserComponent from './components/admin/UserComponent.jsx'
import LoginForm from './components/Auth/LoginForm.jsx'
import Forbidden from './components/admin/Forbidden.jsx'
import ProtectedRoute from './components/admin/ProtectedRoute.jsx'
import RegisterForm from './components/Auth/RegisterForm.jsx'






function App() {
  

  return (
    <>
    <BrowserRouter>
      <div className='app-bar'>
        
      
        <Routes>


          <Route path='/login' element = {<LoginForm/>}></Route>

          <Route path='/register' element = {<RegisterForm/>}></Route>

          <Route path='/forbidden' element= {<Forbidden/>}></Route>
          
          <Route path='/' element = {<HomePage/>}></Route>       

          <Route path="/search" element={<SearchResults />}></Route>

          <Route path="/products" element={<ListProducts />}></Route>
          
          <Route path='/products/:productId' element = {<ProductDetailPage/>}></Route>




                   


          <Route path="/change-password" element={<ProtectedRoute roles={['USER','ADMIN']} />}>
                    <Route path='' element = {<ChangePasswordComponent/>}/> 
          </Route>

          <Route path="/update-user" element={<ProtectedRoute roles={['USER','ADMIN']} />}>
                    <Route path='' element = {<UpdateUserComponent/>}/> 
          </Route>

          <Route path="/profile" element={<ProtectedRoute roles={['USER','ADMIN']} />}>
                    <Route path='' element = {<AddressComponent/>}/> 
          </Route>

          <Route path="/add-address" element={<ProtectedRoute roles={['USER','ADMIN']} />}>
                    <Route path='' element = {<AddAddressForm/>}/> 
          </Route>

          <Route path="/update-address/:addressId" element={<ProtectedRoute roles={['USER','ADMIN']} />}>
                    <Route path='' element = {<UpdateAddressForm/>}/> 
          </Route>

          <Route path="/orders" element={<ProtectedRoute roles={['USER','ADMIN']} />}>
                    <Route path='' element = {<OrdersPage/>}/> 
          </Route>

          <Route path="/reviews/:productId" element={<ProtectedRoute roles={['USER','ADMIN']} />}>
                    <Route path='' element = {<ReviewPage/>}/> 
          </Route>

          <Route path="/added-to-basket" element={<ProtectedRoute roles={['USER']} />}>
                    <Route path='' element = {<AddedToCartPage/>}/> 
          </Route>

          <Route path="/cart" element={<ProtectedRoute roles={['USER']} />}>
                    <Route path='' element = {<CartPage/>}/> 
          </Route>




    





          <Route path="/admin/users" element={<ProtectedRoute roles={['ADMIN']} />}>
                    <Route path='' element = {<AdminListUsers/>}/> 
          </Route>

          <Route path="/admin/products" element={<ProtectedRoute roles={['ADMIN']} />}>
                    <Route path='' element = {<AdminListProducts/>}/> 
          </Route>

          <Route path="/admin/products/createProduct" element={<ProtectedRoute roles={['ADMIN']} />}>
                    <Route path='' element = {<ProductComponent/>}/> 
          </Route>

          <Route path="/admin/products/updateProduct/:id" element={<ProtectedRoute roles={['ADMIN']} />}>
                    <Route path='' element = {<ProductComponent/>}/> 
          </Route>

          <Route path="/admin/users/createUser" element={<ProtectedRoute roles={['ADMIN']} />}>
                    <Route path='' element = {<UserComponent/>}/> 
          </Route>

          <Route path="/admin/users/updateUser/:id" element={<ProtectedRoute roles={['ADMIN']} />}>
                    <Route path='' element = {<UserComponent/>}/> 
          </Route>

          <Route path="/admin/users/:userId/profile" element={<ProtectedRoute roles={['ADMIN']} />}>
                    <Route path='' element = {<UserProfile/>}/> 
          </Route>




        </Routes>
      </div>
      </BrowserRouter>
    </>
  )
}

export default App
