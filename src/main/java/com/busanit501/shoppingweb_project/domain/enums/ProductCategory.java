package com.busanit501.shoppingweb_project.domain.enums;

public enum ProductCategory {
    // ✅ 의류 쇼핑몰 컨셉에 맞는 카테고리로 변경
    TOP("상의"),
    BOTTOM("하의"),
    OUTER("아우터"),
    DRESS("원피스"),
    SHOES("신발"),
    BAG("가방"),
    ACC("액세서리"),
    UNKNOWN("기타/알 수 없음");
    private final String koreanName;

    ProductCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }


    public static ProductCategory fromKoreanName(String koreanName) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.getKoreanName().equals(koreanName)) {
                return category;
            }
        }
        return UNKNOWN;
    }
}