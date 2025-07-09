このER図は、ECサイトの顧客・商品・カート・注文情報の構造と、それらの関係性（1対多や多対多）を示したものです。
<div class="mermaid">
erDiagram
    CUSTOMER ||--o{ ORDER : has
    ORDER ||--o{ ORDER_ITEM : contains
    PRODUCT ||--o{ ORDER_ITEM : included_in
    PRODUCT ||--o{ CART_ITEM : has
    CART ||--o{ CART_ITEM : contains
    CUSTOMER ||--o| CART : has_one
    PRODUCT ||--o{ PRODUCT_IMAGE : has

    CUSTOMER {
        string customer_id PK "顧客ID（会員登録者）"
        string customer_name "氏名"
        string email "メールアドレス"
        string password "パスワード（ハッシュ化）"
        string shipping_address "配送先住所"
        string phone_number "電話番号"
        datetime created_at "登録日時"
        datetime updated_at "更新日時"
    }

    PRODUCT {
        string product_id PK "商品ID"
        string product_name "商品名"
        text description "商品説明"
        text material "素材"
        decimal price "価格"
        integer stock_quantity "在庫数"
        datetime created_at "登録日時"
        datetime updated_at "更新日時"
    }

    PRODUCT_IMAGE {
        string image_id PK "画像ID"
        string product_id FK "商品ID"
        string image_url "画像URL"
        int display_order "表示順序"
        boolean is_main "メイン画像フラグ"
    }

    ORDER {
        string order_id PK "注文ID"
        string customer_id FK "顧客ID（会員の場合）"
        string guest_name "購入者氏名（非会員の場合）"
        string guest_email "購入者メールアドレス（非会員の場合）"
        string guest_shipping_address "配送先住所（非会員の場合）"
        string guest_phone_number "電話番号（非会員の場合）"
        decimal total_amount "合計金額"
        string shipping_fee "送料"
        datetime order_date "注文日時"
        string order_status "注文ステータス"
    }

    ORDER_ITEM {
        string order_item_id PK "注文明細ID"
        string order_id FK "注文ID"
        string product_id FK "商品ID"
        int quantity "数量"
        decimal item_price "商品単価（注文時の価格）"
    }

    CART {
        string cart_id PK "カートID"
        string customer_id FK "顧客ID（会員の場合）"
        datetime created_at "作成日時"
        datetime updated_at "更新日時"
    }

    CART_ITEM {
        string cart_item_id PK "カート明細ID"
        string cart_id FK "カートID"
        string product_id FK "商品ID"
        int quantity "数量"
    }
