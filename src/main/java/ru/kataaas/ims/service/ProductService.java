package ru.kataaas.ims.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.kataaas.ims.dto.CategoryDTO;
import ru.kataaas.ims.dto.CreateProductDTO;
import ru.kataaas.ims.dto.ProductDTO;
import ru.kataaas.ims.dto.ProductResponse;
import ru.kataaas.ims.entity.CartProductsEntity;
import ru.kataaas.ims.entity.CategoryEntity;
import ru.kataaas.ims.entity.ProductEntity;
import ru.kataaas.ims.entity.VendorEntity;
import ru.kataaas.ims.mapper.ProductMapper;
import ru.kataaas.ims.repository.CategoryRepository;
import ru.kataaas.ims.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    private final VendorService vendorService;

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductService(ProductMapper productMapper,
                          VendorService vendorService,
                          ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productMapper = productMapper;
        this.vendorService = vendorService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Optional<ProductEntity> findById(Long id) {
        return productRepository.findById(id);
    }

    public Set<ProductEntity> findProductsByIds(Set<Long> ids) {
        return productRepository.findByIdIsIn(ids);
    }

    public ProductResponse fetchProductsByVendorName(String vendorName, int pageNo, int pageSize) {
        Page<ProductEntity> products = productRepository.findAllByVendor_Name(vendorName, PageRequest.of(pageNo, pageSize));
        return productMapper.toProductResponse(products);
    }

    public void setQuantityProduct(Long id, int quantity) {
        ProductEntity product = productRepository.findById(id).orElse(null);
        if (product != null)
            product.setQuantity(quantity);
    }

    public ProductDTO create(CreateProductDTO productDTO, Long vendorId) {
        ProductEntity product = new ProductEntity();
        VendorEntity vendor = vendorService.findById(vendorId);
        CategoryEntity category = getCategoryBySubcategory(productDTO.getSubcategory());

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setCategory(category);
        product.setVendor(vendor);

        ProductEntity savedProduct = productRepository.save(product);
        return productMapper.toProductDTO(savedProduct);
    }

    public CategoryEntity getCategoryBySubcategory(String subcategory) {
        return categoryRepository.getBySubcategory(subcategory);
    }

    public List<CategoryDTO> fetchAllCategories() {
        return categoryRepository.findAll().stream()
                .map(productMapper::toCategoryDTO).collect(Collectors.toList());
    }

    public int getQuantityById(Long id) {
        return productRepository.getQuantityProduct(id);
    }
}
