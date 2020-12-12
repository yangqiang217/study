package com.yq.hooker.util;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public enum PhoneInfoPool {
    INS;

    public int getRandomIndex() {
        Random random = new Random();
        return random.nextInt(brands.length);
    }

    public String getBrand(int index) {
        return brands[index];
    }

    public String getModel(int index) {
        return models[index];
    }

    public String getRandomRelease() {
        return UUID.randomUUID().toString();
    }

    // 返回随机电话号码
    public String getMobile() {
        while (true) {
            String randomPhone = randomPhone();
            if (isMobileNO(randomPhone)) {
                return randomPhone;
            }
        }
    }

    // 判断是否电话格式
    private boolean isMobileNO(String mobiles) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        return pattern.matcher(mobiles).matches();
    }

    // 产生随机电话号码格式数字
    private String randomPhone() {
        String phone = "1";
        Random random = new Random();
        int nextInt = random.nextInt(3);
        if (nextInt == 0) {
            phone = phone + "3" + randomNumber();
        } else if (nextInt == 1) {
            phone = phone + "5" + randomNumber();
        } else {
            phone = phone + "8" + randomNumber();
        }
        return phone;
    }

    // 生成长度为9的随机数
    private String randomNumber() {
        Random random = new Random();
        int nextInt = random.nextInt(900000000) + 100000000;
        int abs = Math.abs(nextInt);
        return String.valueOf(abs);
    }

    private String[] brands = new String[]{
        "HUAWEI", "OPPO","HONOR","OPPO","vivo","OPPO","HONOR","OPPO","HONOR",
        "xiaomi","vivo","HUAWEI","Xiaomi","OPPO","Xiaomi","vivo","vivo","Xiaomi","Meizu","Xiaomi","vivo","OPPO",
        "GIONEE","vivo","Xiaomi","Xiaomi","OPPO","Xiaomi","HUAWEI","Letv","vivo","HUAWEI","Honor","Meizu","HONOR",
        "samsung","OPPO","xiaomi","HUAWEI","ZTE","HONOR","Xiaomi","vivo","vivo","OPPO","HUAWEI","HUAWEI","HUAWEI",
        "Xiaomi","Meizu","Lenovo","GIONEE","OPPO","Xiaomi","Xiaomi","HUAWEI","Xiaomi","vivo","HONOR","Meizu",
        "HUAWEI","LeEco","Xiaomi","OPPO","vivo","Xiaomi","HUAWEI","HUAWEI","OPPO","OPPO","OPPO","HONOR","Xiaomi",
        "Meizu","vivo","xiaomi","OPPO","GIONEE","Xiaomi","alps","Xiaomi","OPPO","HUAWEI","OPPO","OPPO","HONOR",
        "HUAWEI","samsung","vivo","OPPO","Meizu","Meizu","Xiaomi","Huawei","Xiaomi","HUAWEI","Xiaomi","HONOR",
        "OPPO","Xiaomi","vivo","xiaomi","Xiaomi","Letv","GIONEE","OPPO","vivo","nubia","Xiaomi","HUAWEI","HUAWEI",
        "OPPO","OPPO","HUAWEI","vivo","HUAWEI","honor","OPPO","Huawei","Xiaomi","Xiaomi","Xiaomi","OPPO","HUAWEI",
        "GIONEE","vivo","HONOR","Letv","vivo","HUAWEI","koobee","Xiaomi","OPPO","HONOR","OPPO","Xiaomi","HONOR",
        "Meizu","vivo","Xiaomi","vivo","vivo","Xiaomi","vivo","HUAWEI","HONOR","vivo","Xiaomi","vivo","xiaomi",
        "Meizu","OPPO","OPPO","OPPO","HUAWEI","Xiaomi","GIONEE","OPPO","vivo","xiaomi","OPPO","OPPO","OPPO","OPPO",
        "vivo","SMARTISAN","GIONEE","Nokia","vivo","samsung","OPPO","OPPO","Xiaomi","HONOR","HONOR","360","OPPO",
        "Meizu","xiaomi","HONOR","samsung","OPPO","HUAWEI","Xiaomi","OPPO","LeEco","KOPO","ZUK","HONOR","vivo",
        "Xiaomi","vivo","vivo","vivo","OPPO","vivo","Xiaomi","honor","vivo","OPPO","Coolpad","HONOR","HUAWEI",
        "OPPO","HUAWEI","KOPO","vivo","HUAWEI","OPPO","HUAWEI","vivo","vivo","HUAWEI","HUAWEI","Meizu","Meizu",
        "Xiaomi","HONOR","OPPO","HONOR","vivo","HONOR","xiaomi","OPPO","Xiaomi","OPPO","vivo","vivo","htc","Meizu",
        "vivo","OPPO","OPPO","OPPO","vivo","vivo","vivo","LeEco","OPPO","vivo","HUAWEI","OPPO","OPPO","OPPO","vivo",
        "OPPO","honor","Letv","samsung","HONOR","HUAWEI","HUAWEI","vivo","OPPO","Meizu","vivo","HONOR","Huawei",
        "HUAWEI","vivo","vivo","OPPO","vivo","OPPO","HONOR","HONOR","honor","xiaomi","vivo","OPPO","vivo","vivo",
        "HUAWEI","xiaomi","GIONEE","OPPO","HUAWEI","Xiaomi","360","GIONEE","xiaomi","HUAWEI","HUAWEI","OPPO","OPPO",
        "vivo","OPPO","Xiaomi","HUAWEI","OPPO","honor","vivo","HUAWEI","OPPO","OPPO","xiaomi","Xiaomi","4G+","vivo",
        "Xiaomi","HUAWEI","vivo","vivo","vivo","OPPO","vivo","OPPO","SMARTISAN","HONOR","vivo","Xiaomi","vivo",
        "Xiaomi","ZUK","Meizu","OPPO","HONOR","OPPO","OPPO","LeEco","OPPO","Xiaomi","HONOR","OPPO","ivvi","vivo",
        "HUAWEI","OPPO","OPPO","HUAWEI","xiaomi","nubia","OPPO","Xiaomi","OPPO","OPPO","OPPO","Honor","Xiaomi","samsung",
        "OPPO","xiaomi","OPPO","OPPO","HUAWEI","OPPO","vivo","vivo","htc","vivo","Meizu","HONOR","OPPO","HUAWEI","vivo",
        "Xiaomi","Xiaomi","OPPO","HUAWEI","Xiaomi","HUAWEI","Xiaomi","vivo","vivo","vivo","xiaomi","OPPO","Meizu","Xiaomi",
        "vivo","vivo","HONOR","samsung","Xiaomi","xiaomi","Xiaomi","Xiaomi","vivo","LeEco","OPPO","OPPO","Xiaomi","HONOR","HONOR","Xiaomi"
    };


    private String[] models = new String[]{
        "VTR-AL00", "OPPO A59s", "BND-AL10", "OPPO R9m","vivo X6S A", "OPPO A59s", "BLN-AL10", "PACM00", "ATH-AL00", "Redmi 5 Plus",
        "vivo X9i", "TRT-TL10A", "MI 4LTE", "OPPO R9sk", "Redmi 4X", "vivo Y67L", "vivo Y85A", "MI 6", "MX6", "MIX 2", "vivo Y75A",
        "PACM00", "GN5003", "vivo X20Plus A", "Redmi Note 3", "Redmi Note 4X", "OPPO A77t", "Redmi Note 4", "NCE-AL00", "x600",
        "vivo Y51", "HUAWEI NXT-AL10", "CHM-UL00", "M5 Note", "MYA-TL10", "SM-G9008V", "OPPO R7s", "Redmi Note 5", "HUAWEI NXT-AL10",
        "ZTE C2017", "KIW-AL10", "MI 8 SE", "vivo X20Plus", "vivo Xplay6", "PACT00", "BLA-AL00", "HUAWEI TAG-AL00", "TRT-AL00A",
        "Redmi 4X", "M3s", "Lenovo X2Pt5", "GN8003", "OPPO A73", "MI MAX 2", "MI 6", "MHA-AL00", "MI 4S", "vivo Y55","KNT-AL20",
        "PRO 5","MHA-AL00","Le X620","MI 4LTE","OPPO R11 Plus","vivo Y66","Mi Note 3","FIG-TL00","EML-AL00","A31","OPPO A57",
        "OPPO A53","ATH-TL00H","MI PAD 2","M1 E","vivo Y53L","Redmi Note 4X","OPPO A59m","GN5003","MI 6","M3 Max","MI 2S",
        "OPPO A57","HUAWEI RIO-AL00","OPPO A57t","OPPO A73","BLN-AL40","TRT-AL00A","SM-G9350","vivo X5S L","R7Plusm","m1 metal",
        "M721C","MI NOTE LTE","HUAWEI G750-T01","MI MAX","TRT-AL00A","MI 6","DUK-AL20","OPPO R9s","Redmi Note 4X","vivo X6S A",
        "Redmi Note 4X","Redmi Note 2","X800","S9","OPPO A59s","vivo Y85A","NX573J","Mi Note 3","BKL-AL20","BAC-AL00","OPPO A59m",
        "OPPO R9tm","ALP-AL00","vivo Y66L","SLA-TL10","FRD-AL10","OPPO R11","HUAWEI P7-L00","Redmi Note 4","Redmi Note 4",
        "Redmi Note 3","OPPO R9m","BAC-AL00","GIONEE S10","vivo X7","LND-AL40","Letv X500","vivo Y85A","EVA-AL10","koobee M9",
        "Redmi 3X","OPPO A57","PRA-AL00X","PACM00","MI 6","CAM-TL00","MEIZU M6","vivo Y79A","MI 5s Plus","vivo X9s Plus L",
        "vivo Y71A","Redmi Note 3","vivo Y55A","HUAWEI TAG-AL00","CAM-AL00","vivo Xplay5A","MI MAX 2","vivo Y75A","Redmi 5 Plus",
        "m3","OPPO R9s Plus","OPPO R9 Plustm A","OPPO R9km","FIG-AL10","MI 6","GIONEE F6","OPPO R11 Plusk","vivo X9Plus","MI 5X",
        "OPPO A83","OPPO R9m","OPPO A59s","OPPO R11","vivo X9s","OS105","GIONEE S10","TA-1000","vivo Y66","SM-A7100","OPPO R9sk",
        "A31","MI 5s Plus","NEM-AL10","CAM-TL00","1501_M02","OPPO R9km","M3X","MI 5X","BND-AL10","SM-A7000","OPPO A57","TRT-AL00A",
        "Redmi Note 2","OPPO A53","Le X620","KOPO L8","ZUK Z2131","BLN-AL10","vivo Y51A","MI MAX 2","vivo X9i","vivo X20A","vivo Y79",
        "OPPO A33","vivo Y79A","Redmi Note 4","FRD-AL10","vivo Y75A","OPPO R11 Plus","C106","BLN-AL10","HUAWEI VNS-AL00","OPPO R11s",
        "VTR-AL00","KOPO T9","vivo X7Plus","FLA-AL20","OPPO A83t","EDI-AL10","vivo X9","vivo Y79","WAS-AL00","TRT-AL00A","M5 Note",
        "Meizu S6","MI 6","NEM-AL10","OPPO A37t","LLD-AL10","vivo Y55A","JMM-AL00","Redmi Note 5","OPPO R7s","MI 5C","OPPO R9s Plus",
        "vivo Y67","vivo Y66","HTC D728w","MX4","vivo Y71A","OPPO R7s","OPPO R11","OPPO R9s Plus","vivo Y66","vivo Y66","vivo X20",
        "Le X621","OPPO R9m","vivo X9s L","VKY-AL00","OPPO A33m","PACM00","OPPO R11","vivo X20","OPPO R9s Plus","FRD-AL10","Letv X501",
        "SM-G9200","AUM-AL20","FIG-AL10","FIG-AL10","vivo Xplay5A","OPPO A59s","Meizu S6","vivo X9s","BLN-AL40","C8817D","SLA-AL00",
        "vivo Y66","vivo Y55A","OPPO R9s Plust","vivo X21UD A","OPPO A59s","BLN-TL10","DLI-AL10","FRD-AL10","Redmi Note 4X","vivo V3",
        "A31","vivo X20","vivo Y66","HUAWEI RIO-AL00","Redmi 5 Plus","GN8002S","OPPO A73t","BAC-AL00","MI 5","1801-A01","GN9012",
        "Redmi 5 Plus","MHA-AL00","PIC-AL00","OPPO A33m","OPPO A57","vivo X9s L","OPPO R9s Plus","Redmi Note 2","VKY-AL00","OPPO R7",
        "FRD-AL10","vivo X7Plus","HUAWEI RIO-AL00","OPPO R9m","OPPO A79k","Redmi 5 Plus","MI MAX 2","4G+","vivo V3","Mi Note 3",
        "VTR-AL00","vivo Y71","vivo Z1","vivo X20A","OPPO A59s","vivo Y66","OPPO R11","OS105","BND-AL00","vivo X5Max V","MI 5C",
        "vivo X5Pro D","Redmi 4X","ZUK Z2121","M621C-S","OPPO R9tm","STF-AL00","OPPO R9tm","OPPO A33","Le X520","OPPO A79kt",
        "Redmi Note 4","CAM-AL00","OPPO A77","ivvi F2-T","vivo X20A","NCE-AL00","OPPO R9m","OPPO R9m","TRT-TL10","MI 5X","NX529J",
        "OPPO R9km","MIX 2S","PACT00","OPPO R11","OPPO A57","CHE-TL00H","HM NOTE 1LTE","SM-C7000","OPPO R9s","Redmi 5 Plus","PACT00",
        "OPPO A83t","SLA-AL00","OPPO A33","vivo X5M","vivo X7","HTC X9u","vivo Y66","M5 Note","STF-AL10","OPPO R11st","HUAWEI NXT-AL10",
        "vivo Y75A","Redmi 4A","Redmi Note 4","OPPO R9s Plus","HUAWEI MLA-AL10","Redmi 3S","HUAWEI NXT-TL00","Redmi Note 3","vivo X9",
        "vivo Y51","vivo Y55","Redmi Note 5A","OPPO A73t","M6 Note","Redmi 3X","vivo Y55","vivo X9","BLN-AL10","SM-G9200","Redmi 3S",
        "Redmi 5 Plus","MI 5s Plus","2014813","vivo Y55L","Le X620","OPPO A37m","OPPO R9sk","MI MAX 2","KIW-AL10","PRA-AL00","Redmi Note 3"
    };
}
