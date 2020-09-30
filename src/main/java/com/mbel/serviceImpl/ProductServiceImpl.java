package com.mbel.serviceImpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbel.config.JwtAuthenticationFilter;
import com.mbel.constants.Constants;
import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.ProductDto;
import com.mbel.dto.SaveProductSetDto;
import com.mbel.model.Product;
import com.mbel.model.ProductSet;
import com.mbel.model.ProductSetModel;


@Service("ProductServiceImpl")
public class ProductServiceImpl  {
	@Autowired 
	ProductDao productDao;

	@Autowired 
	ProductSetDao productSetDao;

	@Autowired
	private JwtAuthenticationFilter jwt;

	@Autowired
	private ModelMapper modelMapper;


	public ResponseEntity<Map<String, String>> save(Product product) {
		Map<String, String> response = new HashMap<>();
		List<Product>allproduct =productDao.findAll();
		product.setCreatedAt(LocalDateTime.now());
		product.setUpdatedAt(LocalDateTime.now());
		product.setUserId(jwt.getUserdetails().getUserId());
		product.setActive(true);
		product.setSet(false);
		product.setDisplay(false);
		if(isObicNoDuplicated(allproduct,product.getObicNo())) {
			response.put(Constants.MESSAGE, "ObicNo Already Present");
			response.put("ObicNo",product.getObicNo());
			return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
		}
		if(product.getSort()==0) {
			assignSortValue(product,allproduct);
		}
		if(!allproduct.isEmpty()&&
				isSortValueAlreadyPresent(allproduct,product.getSort())) {
			return reArrangeProductDataBySort(allproduct,product,response);
		}else {
			productDao.save(product);
			response.put(Constants.MESSAGE, "Product saved");
			response.put("product",product.getProductName());
			return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
		}
	}



	private boolean isObicNoDuplicated(List<Product> allproduct, String obicNo) {
		List<Product>obicProduct=allproduct.stream()
				.filter(predicate->predicate.getObicNo().equals(obicNo))
				.collect(Collectors.toList());
		return obicProduct.isEmpty()?false:true;
	}

	private void assignSortValue(Product product, List<Product> allproduct) {
		allproduct.sort(Comparator.comparingInt(Product::getSort));
		product.setSort(allproduct.get(allproduct.size()-1).getSort()+1);
	}

	private ResponseEntity<Map<String, String>> reArrangeProductDataBySort(List<Product> allproduct, Product product, Map<String, String> response) {
		List<Product>productListAfterSortValue=allproduct.stream()
				.filter(predicate->predicate.getSort()>=product.getSort())
				.collect(Collectors.toList());
		List<Product>sortedProductList=arrangeProductbySortField(productListAfterSortValue);
		int sortValue=product.getSort()+1;
		List<Product>saveProducts=new ArrayList<>();
		saveProducts.add(product);
		for(int i=0;i<sortedProductList.size();i++) {
			sortedProductList.get(i).setSort(sortValue);
			sortValue++;
			saveProducts.add(sortedProductList.get(i));
		}
		productDao.saveAll(saveProducts);
		response.put(Constants.MESSAGE, "Product saved");
		response.put("product",product.getProductName());
		return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);

	}

	private boolean isSortValueAlreadyPresent(List<Product> allproduct, int sortValue) {
		List<Product>productAvailableWithSort=allproduct.stream()
				.filter(predicate->predicate.getSort()==sortValue)
				.collect(Collectors.toList());
		if(productAvailableWithSort.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}

	public List<Product> getAllProducts() {
		List<Product>product =productDao.findAll();
		List<Product>activeProduct= product.stream()
				.filter(predicate->predicate.isActive()
						&&!predicate.isSet())
				.collect(Collectors.toList());
		return arrangeProductbySortField(activeProduct);
	}

	public List<Product> arrangeProductbySortField(List<Product> activeProduct) {
		activeProduct.sort(Comparator.comparingInt(Product::getSort));
		return activeProduct;


	}

	public List<Product> getAllActiveProductset() {
		List<Product>product =productDao.findAll();
		return product.stream()
				.filter(predicate->predicate.isActive()
						&&predicate.isSet())
				.collect(Collectors.toList());
	}

	public ProductDto getProductsById(int productId) {
		ProductDto productDto =new ProductDto();
		Product product= productDao.findById(productId).orElse(null);
		if(Objects.nonNull(product)) {
			productDto.setProductName(product.getProductName());
			productDto.setDescription(product.getDescription());
			productDto.setPrice(product.getPrice());
			productDto.setMoq(product.getMoq());
			productDto.setLeadTime(product.getLeadTime());
			productDto.setObicNo(product.getObicNo());
			productDto.setQuantity(product.getQuantity());
			productDto.setSet(product.isSet());
			productDto.setActive(product.isActive());
			productDto.setCreatedAt(product.getCreatedAt());
			productDto.setUpdatedAt(product.getUpdatedAt());
			productDto.setUserId(jwt.getUserdetails().getUserId());
			productDto.setCurrency(product.getCurrency());
			productDto.setSort(product.getSort());
			productDto.setColor(product.getColor());
			productDto.setDisplay(product.isDisplay());
			productDto.setUser(jwt.getUserdetails());

		}
		return productDto;
	}

	public ResponseEntity<Map<String, String>> saveProductSet(SaveProductSetDto productSet) {
		Map<String, String> response = new HashMap<>();
		List<Product>allproduct =productDao.findAll();
		if(productSet.getSort()==0) {
			assignSortValue(productSet,allproduct);
		}
		Product product = new Product();
		product.setProductName(productSet.getProductName());
		product.setDescription(productSet.getDescription());
		product.setPrice(productSet.getPrice());
		product.setMoq(productSet.getMoq());
		product.setLeadTime(productSet.getLeadTime());
		product.setObicNo(productSet.getObicNo());
		product.setQuantity(productSet.getQuantity());
		product.setSet(true);
		product.setActive(true);
		product.setCreatedAt(LocalDateTime.now());
		product.setUpdatedAt(LocalDateTime.now());
		product.setUserId(jwt.getUserdetails().getUserId());
		product.setCurrency(productSet.getCurrency());
		product.setSort(productSet.getSort());
		product.setColor(productSet.getColor());
		product.setDisplay(false);
		if(isObicNoDuplicated(allproduct,product.getObicNo())) {
			response.put(Constants.MESSAGE, "ObicNo Already Present");
			response.put("ObicNo",product.getObicNo());
			return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
		}
		if(!allproduct.isEmpty()&&
				isSortValueAlreadyPresent(allproduct,productSet.getSort())) {
			reArrangeProductDataBySort(allproduct,product, response);
		}else {
			productDao.save(product);
		}

		int id  = product.getProductId();
		if(productSet.getProducts() != null) {
			int setValue  =productSet.getProducts().size();
			List<ProductSet> productSetList =new ArrayList<>();
			for(int i=0;i<setValue;i++) {
				ProductSet newProductSet = new ProductSet();
				newProductSet.setSetId(id);
				newProductSet.setQuantity(productSet.getProducts().get(i).getQuantity());
				newProductSet.setProductComponentId(productSet.getProducts().get(i).getProductId());
				productSetList.add(newProductSet);
			}
			productSetDao.saveAll(productSetList);

		}
		response.put(Constants.MESSAGE, "Product saved");
		response.put("product",product.getProductName());
		return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
	}

	public List<FetchProductSetDto> getAllProductSet() {
		List<FetchProductSetDto> fetchList =new ArrayList<>();
		List<Product> allProducts = productDao.findAll();
		List<ProductSet> allProductSet=productSetDao.findAll();
		List<Product> proSet=allProducts.stream().filter(predicate->predicate.isActive()&&predicate.isSet()).collect(Collectors.toList());
		List<Product> notsetProducts=allProducts.stream().filter(predicate->!predicate.isSet()).collect(Collectors.toList());
		for(int i=0;i<proSet.size();i++) {
			List<ProductSetModel> productList = new ArrayList<>();
			FetchProductSetDto componentSet= new FetchProductSetDto();
			componentSet.setProductId(proSet.get(i).getProductId());
			componentSet.setProductName(proSet.get(i).getProductName());
			componentSet.setDescription(proSet.get(i).getDescription());
			componentSet.setPrice(proSet.get(i).getPrice());
			componentSet.setMoq(proSet.get(i).getMoq());
			componentSet.setLeadTime(proSet.get(i).getLeadTime());
			componentSet.setObicNo(proSet.get(i).getObicNo());
			componentSet.setQuantity(proSet.get(i).getQuantity());
			componentSet.setSet(proSet.get(i).isSet());
			componentSet.setActive(proSet.get(i).isActive());
			componentSet.setCreatedAt(proSet.get(i).getCreatedAt());
			componentSet.setUpdatedAt(proSet.get(i).getUpdatedAt());
			componentSet.setCurrency(proSet.get(i).getCurrency());
			componentSet.setSort(proSet.get(i).getSort());
			componentSet.setDisplay(proSet.get(i).isDisplay());
			componentSet.setColor(proSet.get(i).getColor());
			int productId=proSet.get(i).getProductId();
			List<ProductSet> productsetList= allProductSet.stream().filter(predicate->predicate.getSetId()==productId).collect(Collectors.toList());
			if(productsetList != null) {
				for(int l=0;l< productsetList.size();l++ ) {
					ProductSetModel productSetModel = new ProductSetModel();
					int productComponentId=productsetList.get(l).getProductComponentId();
					Product component =notsetProducts.stream().filter(predicate->predicate.getProductId()==productComponentId)
							.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
								if (list.size() != 1) {
									return null;
								}
								return list.get(0);
							}));
					productSetModel.setProduct(component);
					productSetModel.setQuantity(productsetList.get(l).getQuantity());
					productList.add(productSetModel);
				}
			}


			componentSet.setProducts(arrangebySortField(productList));
			fetchList.add(componentSet);
		}

		return  arrangeProductSetBySortField(fetchList);
	}

	public FetchProductSetDto getProductSetById(int productId) {
		List<Product> allProducts = productDao.findAll();
		List<ProductSet> allProductSet=productSetDao.findAll();
		List<Product> proSet=allProducts.stream().filter(predicate->predicate.isActive()&&predicate.isSet()).collect(Collectors.toList());
		List<Product> notsetProducts=allProducts.stream().filter(predicate->!predicate.isSet()).collect(Collectors.toList());
		Product proCheck =proSet.stream().filter(predicate->predicate.getProductId()==productId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return null;
					}
					return list.get(0);
				}));
		List<ProductSetModel> productList = new ArrayList<>();
		FetchProductSetDto componentSet= new FetchProductSetDto();
		componentSet.setUser(jwt.getUserdetails());
		if(proCheck!=null) {
			componentSet.setProductId(proCheck.getProductId());
			componentSet.setProductName(proCheck.getProductName());
			componentSet.setDescription(proCheck.getDescription());
			componentSet.setPrice(proCheck.getPrice());
			componentSet.setMoq(proCheck.getMoq());
			componentSet.setLeadTime(proCheck.getLeadTime());
			componentSet.setObicNo(proCheck.getObicNo());
			componentSet.setQuantity(proCheck.getQuantity());
			componentSet.setSet(proCheck.isSet());
			componentSet.setActive(proCheck.isActive());
			componentSet.setUserId(proCheck.getUserId());
			componentSet.setCreatedAt(proCheck.getCreatedAt());
			componentSet.setUpdatedAt(proCheck.getUpdatedAt());
			componentSet.setCurrency(proCheck.getCurrency());
			if(proCheck.isSet()) {
				List<ProductSet> productsetList= allProductSet.stream().filter(predicate->predicate.getSetId()==productId).collect(Collectors.toList());
				for(int l=0;l< productsetList.size();l++ ) {
					ProductSetModel productSetModel = new ProductSetModel();
					int productComponentId=productsetList.get(l).getProductComponentId();
					Product component =notsetProducts.stream().filter(predicate->predicate.getProductId()==productComponentId)
							.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
								if (list.size() != 1) {
									return null;
								}
								return list.get(0);
							}));
					productSetModel.setProduct(component);
					productSetModel.setQuantity(productsetList.get(l).getQuantity());
					productList.add(productSetModel);
				}
			}
		}
		componentSet.setProducts(arrangebySortField(productList));
		return componentSet;



	}

	private List<ProductSetModel> arrangebySortField(List<ProductSetModel> productList) {
		productList.sort(Comparator.comparingInt(predicate->predicate.getProduct().getSort()));
		return productList;
	}

	private List<FetchProductSetDto> arrangeProductSetBySortField(List<FetchProductSetDto> fetchList) {
		fetchList.sort(Comparator.comparingInt(FetchProductSetDto::getSort));
		return fetchList;
	}

	public ResponseEntity<Map<String, String>> getupdateById(int productId, @Valid Product productionDetails) {
		Map<String, String> response = new HashMap<>();
		List<Product>allproduct =productDao.getActiveProducts();
		if(isObicNoEdited(productionDetails,allproduct)) {
			if(isObicNoDuplicated(allproduct,productionDetails.getObicNo())) {
				response.put(Constants.MESSAGE, "ObicNo Already Present");
				response.put("ObicNo",productionDetails.getObicNo());
				return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
			}
		}
		if(productionDetails.getSort()==0) {
			assignSortValue(productionDetails,allproduct);
		}else {
			if(isSortValueEditted(productId,allproduct,productionDetails)) {
				productionDetails.setProductId(productId);
				productionDetails.setActive(true);
				productionDetails.setSet(false);
				productionDetails.setUpdatedAt(LocalDateTime.now());
				return reArrangeProductDataBySort(allproduct,productionDetails,productId);
			}
		}
		productionDetails.setProductId(productId);
		productionDetails.setActive(true);
		productionDetails.setSet(false);
		productionDetails.setUpdatedAt(LocalDateTime.now());
		try {
			productDao.save(productionDetails);
			response.put(Constants.MESSAGE, "product updated");
			response.put("product",productionDetails.getProductName());
			return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
		}catch(Exception ex) {
			response.put(Constants.MESSAGE, "product updation failed");
			response.put("error",ex.getMessage());
			return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
		}

	}

	private boolean isObicNoEdited(@Valid Product productionDetails, List<Product> allproduct) {
		return allproduct.stream().filter(predicate->predicate.getProductId()==productionDetails.getProductId()&&
				predicate.getObicNo().equals(productionDetails.getObicNo()))
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return true;
					}
					return false;
				}));
	}



	private boolean isSortValueEditted(int productId, List<Product> allproduct, @Valid Product productionDetails) {
		Product previouslySavedProduct =allproduct.stream().filter(predicate->predicate.getProductId()==productId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return null;
					}
					return list.get(0);
				}));
		productionDetails.setCreatedAt(previouslySavedProduct.getCreatedAt());
		return previouslySavedProduct.getSort()==productionDetails.getSort()?false:true;
	}

	private ResponseEntity<Map<String, String>> reArrangeProductDataBySort(List<Product> allproduct, @Valid Product productionDetails,
			int productId) {
		Map<String, String> response = new HashMap<>();
		List<Product>saveProductsList=new ArrayList<>();
		Product previouslySavedProduct =allproduct.stream().filter(predicate->predicate.getProductId()==productId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return null;
					}
					return list.get(0);
				}));
		List<Product>productListAfterSortValue=new ArrayList<>();
		if(previouslySavedProduct.getSort()>productionDetails.getSort()) {
			productListAfterSortValue=allproduct.stream()
					.filter(predicate->predicate.getSort()>=productionDetails.getSort()
					&& predicate.getSort()<previouslySavedProduct.getSort())
					.collect(Collectors.toList());
			List<Product>sortedProductList=arrangeProductbySortField(productListAfterSortValue);
			saveProductsList.add(productionDetails);
			for(int i=0;i<sortedProductList.size();i++) {
				sortedProductList.get(i).setSort(sortedProductList.get(i).getSort()+1);
				saveProductsList.add(sortedProductList.get(i));
			}
			productDao.saveAll(saveProductsList);
			response.put(Constants.MESSAGE, "product updated");
			response.put("product",productionDetails.getProductName());
			return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
		}else {
			productListAfterSortValue=allproduct.stream()
					.filter(predicate->predicate.getSort()>previouslySavedProduct.getSort()
							&& predicate.getSort()<=productionDetails.getSort())
					.collect(Collectors.toList());
			saveProductsList.add(productionDetails);
			List<Product>sortedProductList=arrangeProductbySortField(productListAfterSortValue);
			for(int i=0;i<sortedProductList.size();i++) {
				sortedProductList.get(i).setSort(sortedProductList.get(i).getSort()-1);
				saveProductsList.add(sortedProductList.get(i));
			}
			productDao.saveAll(saveProductsList);
			response.put(Constants.MESSAGE, "product updated");
			response.put("product",productionDetails.getProductName());
			return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
		}


	}

	public Product deleteProductById(int productId) {
		Product product = productDao.findById(productId).orElse(null);
		if(product!=null) {
			product.setActive(false);
			return productDao.save(product);
		}
		return product;
	}

	public ResponseEntity<Map<String, String>> getupdateProductSetById(int productId, @Valid SaveProductSetDto productSetDetails) {
		Map<String, String> response = new HashMap<>();
		List<Product> allproduct = productDao.getActiveProducts();
		if(isObicNoEdited(productSetDetails, allproduct)) {
			if(isObicNoDuplicated(allproduct,productSetDetails.getObicNo())) {
				response.put(Constants.MESSAGE, "ObicNo Already Present");
				response.put("ObicNo",productSetDetails.getObicNo());
				return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
			}
		}
		Product product=allproduct.stream().filter(predicate->predicate.getProductId()==productId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return null;
					}
					return list.get(0);
				}));
		if(productSetDetails.getSort()==0) {
			assignSortValue(product,allproduct);
		}
		if(product!=null) {
			product.setProductName(productSetDetails.getProductName());
			product.setDescription(productSetDetails.getDescription());
			product.setPrice(productSetDetails.getPrice());
			product.setMoq(productSetDetails.getMoq());
			product.setLeadTime(productSetDetails.getLeadTime());
			product.setQuantity(productSetDetails.getQuantity());
			product.setSet(true);
			product.setActive(true);
			product.setUpdatedAt(LocalDateTime.now());
			product.setUserId(jwt.getUserdetails().getUserId());
			product.setCurrency(productSetDetails.getCurrency());
			product.setColor(productSetDetails.getColor());
			product.setDisplay(productSetDetails.isDisplay());
			product.setObicNo(productSetDetails.getObicNo());
			if(product.getSort()!=productSetDetails.getSort()) {
				reArrangeProductSetDataBySort(allproduct,productSetDetails.getSort(),productId,product);
			}
			productDao.save(product);

			int setValue  =productSetDetails.getProducts().size();
			productSetDao.deleteBySet(productId);
			List<ProductSet> productSetList =new ArrayList<>();
			for(int i=0;i<setValue;i++) {
				ProductSet productSet = new ProductSet();
				productSet.setSetId(productId);
				productSet.setQuantity(productSetDetails.getProducts().get(i).getQuantity());
				productSet.setProductComponentId(productSetDetails.getProducts().get(i).getProductId());
				productSetList.add(productSet);
			}
			productSetDao.saveAll(productSetList);
			response.put(Constants.MESSAGE, "product updated");
			response.put("product",product.getProductName());
			return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);

		}
		response.put(Constants.MESSAGE, "product not found");
		response.put("product",productSetDetails.getProductName());
		return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
	}

	private void reArrangeProductSetDataBySort(List<Product> allproduct, int sort, int productId, Product product) {
		List<Product>saveProductsList=new ArrayList<>();
		Product previouslySavedProduct =allproduct.stream().filter(predicate->predicate.getProductId()==productId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return null;
					}
					return list.get(0);
				}));
		List<Product>productListAfterSortValue=new ArrayList<>();
		if(previouslySavedProduct.getSort()>sort) {
			productListAfterSortValue=allproduct.stream()
					.filter(predicate->predicate.getSort()>=sort
					&& predicate.getSort()<previouslySavedProduct.getSort())
					.collect(Collectors.toList());
			List<Product>sortedProductList=arrangeProductbySortField(productListAfterSortValue);
			product.setSort(sort);
			saveProductsList.add(product);
			for(int i=0;i<sortedProductList.size();i++) {
				sortedProductList.get(i).setSort(sortedProductList.get(i).getSort()+1);
				saveProductsList.add(sortedProductList.get(i));
			}
			productDao.saveAll(saveProductsList);
		}else {
			productListAfterSortValue=allproduct.stream()
					.filter(predicate->predicate.getSort()>previouslySavedProduct.getSort()
							&& predicate.getSort()<=sort)
					.collect(Collectors.toList());
			product.setSort(sort);
			saveProductsList.add(product);
			List<Product>sortedProductList=arrangeProductbySortField(productListAfterSortValue);
			for(int i=0;i<sortedProductList.size();i++) {
				sortedProductList.get(i).setSort(sortedProductList.get(i).getSort()-1);
				saveProductsList.add(sortedProductList.get(i));
			}
			productDao.saveAll(saveProductsList);

		}


	}


	public Product deleteProductSetById(int productId) {
		return deleteProductById(productId);
	}

	public List<FetchProductSetDto> getAllSqlProductSet() {
		int tempSetId = -1;
		List<FetchProductSetDto> fetchList =new ArrayList<>();
		List<Map<Object, Object>> elements = productSetDao.getAll();
		FetchProductSetDto componentSet= new FetchProductSetDto();
		for(int i = 0; i<elements.size(); i++) {
			if(tempSetId!=(Integer)elements.get(i).get("package_id")) {
				tempSetId = (Integer)elements.get(i).get("package_id");
				if(componentSet!=null && componentSet.getProductId()!=0) {
					fetchList.add(componentSet);
					componentSet = new FetchProductSetDto();
				}
				componentSet.setProductId((Integer)elements.get(i).get("package_id"));
				componentSet.setProductName((String)elements.get(i).get("package_name"));
				componentSet.setDescription((String)elements.get(i).get("package_desc"));
				componentSet.setPrice((Double)elements.get(i).get("package_price"));
				componentSet.setMoq((Integer)elements.get(i).get("package_moq"));
				componentSet.setLeadTime((Integer)elements.get(i).get("package_lead"));
				componentSet.setObicNo((String)elements.get(i).get("package_obic"));
				componentSet.setQuantity((Integer)elements.get(i).get("package_qty"));
				componentSet.setSet((boolean)elements.get(i).get("package_set"));
				componentSet.setActive((boolean)elements.get(i).get("package_active"));
				componentSet.setCreatedAt(((Timestamp) elements.get(i).get("package_created")).toLocalDateTime());
				componentSet.setUpdatedAt(((Timestamp) elements.get(i).get("package_update")).toLocalDateTime());
				singleProductCreation(elements.get(i),componentSet);

			}else {
				singleProductCreation(elements.get(i),componentSet);

			}
			if(i==elements.size()-1) {
				fetchList.add(componentSet);
			}

		}


		return fetchList;

	}

	private void singleProductCreation(Map<Object, Object> elements, FetchProductSetDto componentSet) {
		List<ProductSetModel> productSetModelList = new ArrayList<>();
		Product component = new Product();
		component.setProductId((Integer)elements.get("product_id"));
		component.setProductName((String)elements.get("product_name"));
		component.setDescription((String)elements.get("description"));
		component.setPrice((Double)elements.get("price"));
		component.setMoq((Integer)elements.get("moq"));
		component.setLeadTime((Integer)elements.get("lead_time"));
		component.setObicNo((String)elements.get("obic_no"));
		component.setQuantity((Integer)elements.get("qty"));
		component.setActive((boolean)elements.get("active"));
		component.setSet((boolean)elements.get("is_set"));
		component.setCreatedAt(((Timestamp) elements.get("created_at_date_time")).toLocalDateTime());
		component.setUpdatedAt(((Timestamp) elements.get("updated_at_date_time")).toLocalDateTime());
		component.setUserId((Integer)elements.get("user_id"));
		ProductSetModel productSetModel = new ProductSetModel();
		productSetModel.setProduct(component);
		productSetModel.setQuantity((Integer)elements.get("quantity"));
		productSetModelList.add(productSetModel);
		componentSet.setProducts(productSetModelList);

	}

	public FetchProductSetDto getProductSetSqlById(int productId) {
		int tempSetId = -1;
		List<Map<Object, Object>> elements = productSetDao.getProductSetsById(productId);
		FetchProductSetDto componentSet= new FetchProductSetDto();
		for(int i = 0; i<elements.size(); i++) {
			if(tempSetId!=(Integer)elements.get(i).get("package_id")) {
				tempSetId = (Integer)elements.get(i).get("package_id");
				componentSet.setProductId((Integer)elements.get(i).get("package_id"));
				componentSet.setProductName((String)elements.get(i).get("package_name"));
				componentSet.setDescription((String)elements.get(i).get("package_desc"));
				componentSet.setPrice((Double)elements.get(i).get("package_price"));
				componentSet.setMoq((Integer)elements.get(i).get("package_moq"));
				componentSet.setLeadTime((Integer)elements.get(i).get("package_lead"));
				componentSet.setObicNo((String)elements.get(i).get("package_obic"));
				componentSet.setQuantity((Integer)elements.get(i).get("package_qty"));
				componentSet.setSet((boolean)elements.get(i).get("package_set"));
				componentSet.setActive((boolean)elements.get(i).get("package_active"));
				componentSet.setCreatedAt(((Timestamp) elements.get(i).get("package_created")).toLocalDateTime());
				componentSet.setUpdatedAt(((Timestamp) elements.get(i).get("package_update")).toLocalDateTime());
				singleProductCreation(elements.get(i),componentSet);

			}else {
				singleProductCreation(elements.get(i),componentSet);

			}

		}


		return componentSet;


	}



	public List<FetchProductSetDto> getAllProductsAndProductSets() {

		List<Product> allProducts = productDao.getActiveProducts();
		List<ProductSet> allProductSet = productSetDao.findAll();

		List<FetchProductSetDto> fetchList =  allProducts.stream()
				.filter(x -> x.isSet()) // get only Product set
				.map(x -> {
					// map DAO to DTO
					FetchProductSetDto componentSet = this.modelMapper.map(x, FetchProductSetDto.class);
					// get only product in set with componentSet.productId
					List<ProductSetModel> productsetList = allProductSet.stream()
							.filter(predicate-> predicate.getSetId()== componentSet.getProductId())
							.map(predicate -> {
								// map product set object
								Product product = allProducts.stream()
										.filter(p -> p.getProductId() == predicate.getProductComponentId())
										.findFirst()
										.get();
								return new ProductSetModel(product);
							})
							.collect(Collectors.toList());

					componentSet.setProducts(productsetList);
					return componentSet;
				}).collect(Collectors.toList());

		// map individual set with not assigned products
		List<ProductSetModel> individualProducts = allProducts.stream()
				.filter(x -> 
				!x.isSet() && !allProductSet.stream().anyMatch(s -> s.getProductComponentId() == x.getProductId()))
				.map(p -> new ProductSetModel(p))
				.collect(Collectors.toList());

		fetchList.add(new FetchProductSetDto(individualProducts, true));
		return  arrangeProductSetBySortField(fetchList);
	}


}

