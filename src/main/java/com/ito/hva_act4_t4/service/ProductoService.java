package com.ito.hva_act4_t4.service;

import com.ito.hva_act4_t4.dto.ProductoDTO;
import com.ito.hva_act4_t4.entity.Producto;
import com.ito.hva_act4_t4.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Page<ProductoDTO> listarPaginado(Pageable pageable) {
        return productoRepository.findAll(pageable).map(this::convertirADTO);
    }

    public Optional<ProductoDTO> buscarPorId(Long id) {
        return productoRepository.findById(id).map(this::convertirADTO);
    }

    public ProductoDTO crear(ProductoDTO dto) {
        Producto producto = new Producto(dto.getNombre(), dto.getPrecio(), dto.getStock(), dto.getCategoria());
        Producto guardado = productoRepository.save(producto);
        return convertirADTO(guardado);
    }

    public Optional<ProductoDTO> actualizar(Long id, ProductoDTO dto) {
        return productoRepository.findById(id).map(prod -> {
            prod.setNombre(dto.getNombre());
            prod.setPrecio(dto.getPrecio());
            prod.setStock(dto.getStock());
            prod.setCategoria(dto.getCategoria());
            return convertirADTO(productoRepository.save(prod));
        });
    }

    public boolean eliminar(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ProductoDTO convertirADTO(Producto p) {
        return new ProductoDTO(p.getId(), p.getNombre(), p.getPrecio(), p.getStock(), p.getCategoria());
    }
}