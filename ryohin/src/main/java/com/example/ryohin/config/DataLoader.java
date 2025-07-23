package com.example.ryohin.config;

import com.example.ryohin.entity.Product;
import com.example.ryohin.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;


    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        loadSampleProducts();
    }

    private void loadSampleProducts() {
        if (productRepository.count() > 0) {
            return; // すでにデータが存在する場合はスキップ
        }

        List<Product> products = Arrays.asList(
            createProduct(
                "シンプルデスクオーガナイザー",
                "机の上をすっきり整理できる木製オーガナイザー。ペン、メモ、スマートフォンなどを収納できます。",
                "木製",
                500,
                20,
                "/images/desk-organizer.png"

            ),
            createProduct(
                "アロマディフューザー（ウッド）", 
                "天然木を使用したシンプルなデザインのアロマディフューザー。LEDライト付き。", 
                "木製",
                1000,
                15,                
                "/images/aroma-diffuser.png"


            ),
            createProduct(
                "コットンブランケット", 
                "オーガニックコットン100%のやわらかブランケット。シンプルなデザインで様々なインテリアに合います。",
                "コットン", 
                5800, 
                10, 
                "/images/cotton-blanket.png"
            ),
            createProduct(
                "ステンレスタンブラー", 
                "保温・保冷機能に優れたシンプルなデザインのステンレスタンブラー。容量350ml。",
                "ステンレス", 
                2800, 
                30, 
                "/images/tumbler.png"
            ),
            createProduct(
                "ミニマルウォールクロック", 
                "余計な装飾のないシンプルな壁掛け時計。静音設計。",
                "", 
                3200, 
                25, 
                "/images/wall-clock.png"
            ),
            createProduct(
                "リネンクッションカバー", 
                "天然リネン100%のクッションカバー。取り外して洗濯可能。45×45cm対応。",
                "リネン", 
                2500, 
                40, 
                "/images/cushion-cover.png"
            ),
            createProduct(
                "陶器フラワーベース", 
                "手作りの風合いが魅力の陶器製フラワーベース。シンプルな形状で花を引き立てます。",
                "陶器", 
                4000, 
                15, 
                "/images/flower-vase.png"
            ),
            createProduct(
                "木製コースター（4枚セット）", 
                "天然木を使用したシンプルなデザインのコースター。4枚セット。",
                "木製", 
                1800, 
                50, 
                "/images/wooden-coaster.png" 
            ),
            createProduct(
                "キャンバストートバッグ", 
                "丈夫なキャンバス地で作られたシンプルなトートバッグ。内ポケット付き。",
                "", 
                3600, 
                35, 
                "/images/tote-bag.png"
            ),
            createProduct(
                "ガラス保存容器セット", 
                "電子レンジ・食洗機対応のガラス製保存容器。3サイズセット。",
                "ガラス", 
                4500, 
                20, 
                "/images/glass-container.png"
            )
        );
        
        productRepository.saveAll(products);
    }
    
    private Product createProduct(String name, String description, String material, Integer price, Integer stock, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setMaterial(material);
        product.setPrice(price);
        product.setStockQuantity(stock);
        product.setImageUrl(imageUrl);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }
}
