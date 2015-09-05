package ru.datagrabber.data;

/**
 * Created by cobr123 on 05.09.2015.
 */
public enum DataCell {
    EXT_ID {
        @Override
        public String getCaption() {
            return "Внешний ID";
        }
    },
    NAME {
        @Override
        public String getCaption() {
            return "Название";
        }
    },
    ARTICLE {
        @Override
        public String getCaption() {
            return "Артикул";
        }
    },
    MANUFACTURER_COUNTRY {
        @Override
        public String getCaption() {
            return "Страна изготовитель";
        }
    },
    CITY_FROM {
        @Override
        public String getCaption() {
            return "Откуда товары - Город";
        }
    },
    VIDEO {
        @Override
        public String getCaption() {
            return "Видео";
        }
    },
    PRODUCT_URL {
        @Override
        public String getCaption() {
            return "Ссылка на товар";
        }
    },
    MATERIAL {
        @Override
        public String getCaption() {
            return "Материал";
        }
    },
    COLOR {
        @Override
        public String getCaption() {
            return "Цвет";
        }
    },
    SIZE {
        @Override
        public String getCaption() {
            return "Размер";
        }
    },
    PRICE {
        @Override
        public String getCaption() {
            return "Цена";
        }
    },
    CATEGORIES {
        @Override
        public String getCaption() {
            return "Разделы";
        }
    },
    IMG_MAIN {
        @Override
        public String getCaption() {
            return "Основная картинка";
        }
    },
    IMG_OTHERS {
        @Override
        public String getCaption() {
            return "Дополнительные картинки";
        }
    },
    DESC {
        @Override
        public String getCaption() {
            return "Описание товара";
        }
    };


    public abstract String getCaption();
}