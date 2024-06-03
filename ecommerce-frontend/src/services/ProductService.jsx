import axios from "axios";


const REST_API_BASE_URL = 'http://localhost:8080/api/products';



export const listProducts = () => axios.get(REST_API_BASE_URL);

export const createProduct = (product) => axios.post(REST_API_BASE_URL + '/createProduct', product);

export const getProduct = (productId) => axios.get(REST_API_BASE_URL + '/' + productId); 

export const updateProduct = (productId, product) => axios.put(REST_API_BASE_URL + '/updateProduct/' + productId, product);

export const getAddress = (addressId) => axios.get("http://localhost:8080/api/addresses/"  + productId);

export const updateAddress = (addressId, address) => axios.put("http://localhost:8080/api/addresses/updateAddress/" + addressId, address);

export const deleteProduct = (productId) => axios.delete(REST_API_BASE_URL + '/deleteProduct/' + productId);

export const getProductReviews = (productId) => axios.get(REST_API_BASE_URL + '/' + productId + '/reviews');

export const searchProductsByName = (searchQuery) =>  axios.get(REST_API_BASE_URL + '/search', { params: { name: searchQuery } });

export const getCategories = () => axios.get("http://localhost:8080/api/categories");

export const searchProducts = (searchTerm, categoryId) => {
  const params = { searchTerm };
  if (categoryId !== null) {
      params.categoryId = categoryId;
  }
  return axios.get(REST_API_BASE_URL + '/search', { params });
};


