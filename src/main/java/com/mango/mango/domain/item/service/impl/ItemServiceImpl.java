package com.mango.mango.domain.item.service.impl;

import com.mango.mango.domain.contents.entity.Content;
import com.mango.mango.domain.contents.repository.ContentRepository;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.domain.item.dto.request.ItemRequestDto;
import com.mango.mango.domain.item.dto.response.ItemResponseDto;
import com.mango.mango.domain.item.dto.response.SearchItemResponseDto;
import com.mango.mango.domain.item.entity.Item;
import com.mango.mango.domain.item.repository.ItemRepository;
import com.mango.mango.domain.item.service.ItemService;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.global.response.ApiResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ContentRepository contentRepository;
    private final GroupRepository groupRepository;


    // 카테고리 맵 정의
    private static final Map<String, Map<String, String>> CATEGORY_MAP = initializeCategoryMap();
    private static final Map<String, Map<String, String>> P_SUBCATEGORY_MAP = initializePSubcategoryMap();


    // 대분류 카테고리 맵 초기화
    private static Map<String, Map<String, String>> initializeCategoryMap() {
        return new HashMap<>(){{
            put("R", new HashMap<>(){{
                put("01", "곡류");
                put("02", "감자 및 전분류");
                put("03", "당류");
                put("04", "두류");
                put("05", "견과 및 종실류");
                put("06", "채소류");
                put("07", "버섯류");
                put("08", "과일류");
                put("09", "육류");
                put("10", "난류");
                put("11", "어패류 및 기타 수산물");
                put("12", "해조류");
                put("13", "우유류");
                put("14", "유지류");
                put("15", "차류");
                put("16", "음료류");
                put("17", "주류");
                put("18", "조미료류");
                put("19", "조리가공식품류");
                put("20", "기타");
            }});
            put("P", new HashMap<>(){{
                put("01", "과자류, 빵류 또는 떡류");
                put("02", "빙과류");
                put("03", "코코아가공품류 또는 초콜릿류");
                put("04", "당류");
                put("05", "잼류");
                put("06", "두부류 또는 묵류");
                put("07", "식용유지류");
                put("08", "면류");
                put("09", "음료류");
                put("10", "특수영양식품");
                put("11", "특수의료용도식품");
                put("12", "장류");
                put("13", "조미식품");
                put("14", "절임류 또는 조림류");
                put("15", "주류");
                put("16", "농산가공식품류");
                put("17", "식육가공품 및 포장육");
                put("18", "알가공품류");
                put("19", "유가공품류");
                put("20", "수산가공식품류");
                put("21", "동물성가공식품류");
                put("22", "벌꿀 및 화분가공품류");
                put("23", "즉석식품류");
                put("24", "기타식품류");
            }});
            put("D", new HashMap<>(){{
                put("01", "밥류");
                put("02", "빵 및 과자류");
                put("03", "면 및 만두류");
                put("04", "죽 및 스프류");
                put("05", "국 및 탕류");
                put("06", "찌개 및 전골류");
                put("07", "찜류");
                put("08", "구이류");
                put("09", "전/적 및 부침류");
                put("10", "볶음류");
                put("11", "조림류");
                put("12", "튀김류");
                put("13", "나물/숙채류");
                put("14", "생채/무침류");
                put("15", "김치류");
                put("16", "젓갈류");
                put("17", "장아찌/절임류");
                put("18", "장류, 양념류");
                put("19", "유제품류 및 빙과류");
                put("20", "음료 및 차류");
                put("21", "주류");
                put("22", "과일류");
                put("23", "당류");
                put("24", "곡류, 서류 제품");
                put("25", "두류, 견과 및 종실류");
                put("26", "채소, 해조류");
                put("27", "수/조/어/육류");
                put("28", "유지류");
                put("29", "기타");
            }});
        }};
    }


    // P 타입(가공식품) 중분류 카테고리 맵 초기화
    private static Map<String, Map<String, String>> initializePSubcategoryMap() {
        return new HashMap<>() {{
            put("01", new HashMap<>() {{
                put("00", "미분류");
            }});
            put("02", new HashMap<>() {{
                put("00", "미분류");
            }});
            put("03", new HashMap<>() {{
                put("00", "미분류");
                put("01", "코코아가공품류");
                put("02", "초콜릿류");
            }});
            put("04", new HashMap<>() {{
                put("00", "미분류");
                put("01", "설탕류");
                put("02", "당시럽류");
                put("03", "올리고당류");
                put("04", "포도당");
                put("05", "과당류");
                put("06", "엿류");
                put("07", "당류가공품");
            }});
            put("05", new HashMap<>() {{
                put("00", "미분류");
            }});
            put("06", new HashMap<>() {{
                put("00", "미분류");
            }});
            put("07", new HashMap<>() {{
                put("00", "미분류");
                put("01", "식물성유지류");
                put("02", "동물성유지류");
                put("03", "식용유지가공품");
            }});
            put("08", new HashMap<>() {{
                put("00", "미분류");
            }});
            put("09", new HashMap<>() {{
                put("00", "미분류");
                put("01", "다류");
                put("02", "커피");
                put("03", "과일/채소류음료");
                put("04", "탄산음료류");
                put("05", "두유류");
                put("06", "발효음료류");
                put("07", "인삼/홍삼음료");
                put("08", "기타음료");
            }});
            put("10", new HashMap<>() {{
                put("00", "미분류");
                put("01", "조제유류");
                put("02", "영야용 조제식");
                put("03", "성장기용 조제식");
                put("04", "영/유아용 이유식");
                put("05", "체중조절용 조제식품");
                put("06", "임신/수유부용 식품");
                put("07", "고령자용 영양조제식품");
            }});
            put("11", new HashMap<>() {{
                put("00", "미분류");
                put("01", "표준형 영양조제식품");
                put("02", "맞춤형 영양조제식품");
            }});
            put("12", new HashMap<>() {{
                put("00", "미분류");
            }});
            put("13", new HashMap<>() {{
                put("00", "미분류");
                put("01", "식초류");
                put("02", "소스류");
                put("03", "카레");
                put("04", "고춧가루 또는 실고추");
                put("05", "향신료가공품");
                put("06", "식염");
            }});
            put("14", new HashMap<>() {{
                put("00", "미분류");
                put("01", "김치류");
                put("02", "절임류");
                put("03", "조림류");
            }});
            put("15", new HashMap<>() {{
                put("00", "미분류");
                put("01", "발효주류");
                put("02", "증류주류");
                put("03", "기타 주류");
                put("04", "주정");
            }});
            put("16", new HashMap<>() {{
                put("00", "미분류");
                put("01", "전분류");
                put("02", "밀가루류");
                put("03", "땅콩 또는 견과류가공품류");
                put("04", "시리얼류");
                put("05", "찐쌀");
                put("06", "효소식품");
                put("07", "기타 농산가공품류");
            }});
            put("17", new HashMap<>() {{
                put("00", "미분류");
                put("01", "햄류");
                put("02", "소시지류");
                put("03", "베이컨류");
                put("04", "건조저장육류");
                put("05", "양념육류");
                put("06", "식육추출가공품");
                put("07", "식육간편조리세트");
                put("08", "식육함유가공품");
                put("09", "포장육");
            }});
            put("18", new HashMap<>() {{
                put("00", "미분류");
                put("01", "알가공품");
                put("02", "알함유가공품");
            }});
            put("19", new HashMap<>() {{
                put("00", "미분류");
                put("01", "우유류");
                put("02", "가공유류");
                put("03", "산양유");
                put("04", "발효유류");
                put("05", "버터유");
                put("06", "농축유류");
                put("07", "유크림류");
                put("08", "버터류");
                put("09", "치즈류");
                put("10", "분유류");
                put("11", "유청류");
                put("12", "유당");
                put("13", "유단백가수분해식품");
            }});
            put("20", new HashMap<>() {{
                put("00", "미분류");
                put("01", "어육가공품류");
                put("02", "젓갈류");
                put("03", "건포류");
                put("04", "조미김");
                put("05", "한천");
                put("06", "기타 수산물가공품");
            }});
            put("21", new HashMap<>() {{
                put("00", "미분류");
                put("01", "기타식육 또는 기타알제품");
                put("02", "곤충가공식품");
                put("03", "자라가공식품");
                put("04", "추출가공식품");
            }});
            put("22", new HashMap<>() {{
                put("00", "미분류");
                put("01", "벌꿀류");
                put("02", "로열젤리류");
                put("03", "화분가공식품");
            }});
            put("23", new HashMap<>() {{
                put("00", "미분류");
                put("01", "생식류");
                put("02", "즉석섭취/편의식품류");
                put("03", "만두류");
            }});
            put("24", new HashMap<>() {{
                put("00", "미분류");
                put("01", "효모식품");
                put("02", "기타가공품");
            }});
        }};
    }


    // 애플리케이션 시작 시 Item 테이블이 비어있는 경우 공공데이터 전체 Load
    @PostConstruct
    @Transactional
    public void init() {
        // 공공데이터의 전체 데이터를 사용하고 싶으면 아래줄 주석 해제 후 if(false){ 부분 주석 처리
        // if (itemRepository.count() == 0) {
        if(false){
            log.info("Item DB가 비어있습니다. 초기 데이터를 불러옵니다.");
            Instant start = Instant.now();
            loadItemDataFromOpenAPI(null);
            Instant end = Instant.now();

            Duration duration = Duration.between(start, end);
            log.info(String.format("공공데이터 전체 Load 시간: %d분 %d초", duration.toMinutes(), duration.getSeconds() % 60));
        }
    }


    // 매일 자정 당일 공공데이터 Load
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void scheduledLoadItemDataFromOpenAPI() {
        log.info("매일 자정 데이터 갱신 시작: {}", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Instant start = Instant.now();
        loadItemDataFromOpenAPI(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);
        log.info(String.format("공공데이터 당일 Load 시간: %d분 %d초", duration.toMinutes(), duration.getSeconds() % 60));
    }


    // [3-1] 물품 추가 - 물품 추가 검색어 물품들 호출
    @Override
    public ResponseEntity<ApiResponse<SearchItemResponseDto>> searchItems(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by("itemName").ascending());
        Page<Item> itemPage = itemRepository.findByItemNameContainingIgnoreCase(keyword, pageable);

        List<SearchItemResponseDto.searchItems> searchItemList = itemPage.getContent().stream()
                .map(item -> new SearchItemResponseDto.searchItems(
                        item.getItemId(),
                        item.getItemName(),
                        item.getBrandName(),
                        item.getNutriUnit(),
                        item.getNutriCapacity(),
                        item.getNutriKcal()
                ))
                .toList();

        SearchItemResponseDto responseDto = new SearchItemResponseDto(searchItemList);

        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }


    // [3-1] 물품 추가 - 물품 추가 상세 페이지 데이터 호출
    @Override
    public ResponseEntity<ApiResponse<ItemResponseDto>> getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

        ItemResponseDto res = ItemResponseDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .category(item.getCategory())
                .subCategory(item.getSubCategory())
                .brandName(item.getBrandName())
                .nutriUnit(item.getNutriUnit())
                .nutriCapacity(item.getNutriCapacity())
                .nutriKcal(item.getNutriKcal())
                .nutriCarbohydrate(item.getNutriCarbohydrate())
                .nutriProtein(item.getNutriProtein())
                .nutriFat(item.getNutriFat())
                .build();

        return ResponseEntity.ok(ApiResponse.success(res));
    }


    // [3-1] 물품 추가 - 물품 추가
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<?>> addItem(ItemRequestDto req) {
        Group group = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        boolean isOpenItem = req.isOpenItem();

        // Item 저장
        if(isOpenItem){
            Item item = itemRepository.save(
                    Item.builder()
                            .itemName(req.getItemName())
                            .category(req.getCategory())
                            .subCategory(req.getSubCategory())
                            .brandName(req.getBrandName())
                            .storageArea(req.getStorageArea())
                            .nutriUnit(req.getNutriUnit())
                            .nutriCapacity(req.getNutriCapacity())
                            .nutriKcal(req.getNutriKcal())
                            .nutriCarbohydrate(req.getNutriCarbohydrate())
                            .nutriProtein(req.getNutriProtein())
                            .nutriFat(req.getNutriFat())
                            .build()
            );
        }

        // Content 저장
        Content content = contentRepository.save(
                Content.builder()
                        .contentName(req.getItemName())
                        .category(req.getCategory())
                        .subCategory(req.getSubCategory())
                        .brandName(req.getBrandName())
                        .count(req.getCount())
                        .regDate(req.getRegDate())
                        .expDate(req.getExpDate())
                        .storageArea(req.getStorageArea())
                        .memo(req.getMemo())
                        .nutriUnit(req.getNutriUnit())
                        .nutriCapacity(req.getNutriCapacity())
                        .nutriKcal(req.getNutriKcal())
                        .nutriCarbohydrate(req.getNutriCarbohydrate())
                        .nutriProtein(req.getNutriProtein())
                        .nutriFat(req.getNutriFat())
                        .group(group)
                        .build()
        );
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    // 공공데이터를 로드하여 Item 테이블에 저장
    @Transactional
    public void loadItemDataFromOpenAPI(String crtrYmd) {
        log.info("공공데이터 로드 시작: {}", crtrYmd == null ? "전체 데이터" : "날짜별 데이터(" + crtrYmd + ")");
        try {
            Set<Item> itemList = new HashSet<>();
            int pageNo = 1;
            boolean hasMoreData = true;

            while (hasMoreData) {
                String response = fetchDataFromApi(pageNo, crtrYmd);
                if (response.contains("<resultCode>03</resultCode>"))   hasMoreData = false;
                else {
                    log.info("현재 {}개 Load", itemList.size());
                    itemList.addAll(parseItemsFromXml(response));
                    pageNo++;
                }
            }
            itemRepository.saveAll(itemList);
            log.info("총 {}개의 Item을 저장했습니다.", itemList.size());
        } catch (Exception e) {
            log.error("공공데이터 로드 중 오류 발생", e);
            throw new RuntimeException("데이터 로드 실패", e);
        }
    }


    // 공공데이터 API 호출하여 XML 응답 반환
    private String fetchDataFromApi(int pageNo, String crtrYmd) throws Exception {
        StringBuilder urlBuilder = new StringBuilder("http://api.data.go.kr/openapi/tn_pubr_public_nutri_info_api");
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", "UTF-8"))
                .append("=0hB2z2n6AIt7ctmKwKV%2B7wztip2u%2FkivDHxgioBPEi%2B8o9fTytLukgPgNnePyqAf86vMBRja2f88435nLpTpjg%3D%3D");
        urlBuilder.append("&").append(URLEncoder.encode("pageNo", "UTF-8")).append("=").append(pageNo);
        urlBuilder.append("&").append(URLEncoder.encode("numOfRows", "UTF-8")).append("=10000");
        urlBuilder.append("&").append(URLEncoder.encode("type", "UTF-8")).append("=xml");
        if (crtrYmd != null)     urlBuilder.append("&").append(URLEncoder.encode("crtrYmd", "UTF-8")).append("=").append(crtrYmd);

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300 ? conn.getInputStream() : conn.getErrorStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) sb.append(line);
            return sb.toString();
        } finally {
            conn.disconnect();
        }
    }


    // XML 응답을 파싱하여 Item 객체 리스트 생성
    private Set<Item> parseItemsFromXml(String xmlResponse) throws Exception {
        Set<Item> items = new HashSet<>();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlResponse)));
        NodeList itemNodeList = document.getElementsByTagName("item");

        for (int i = 0; i < itemNodeList.getLength(); i++) {
            Element itemElement = (Element) itemNodeList.item(i);
            String itemName = itemElement.getElementsByTagName("foodNm").item(0).getTextContent().replace("_", " ");
            String foodCd = itemElement.getElementsByTagName("foodCd").item(0).getTextContent();
            String dataDiv = foodCd.substring(0, 1);
            String categoryCode = foodCd.substring(2, 4);
            String subCategoryCode = foodCd.substring(8, 10);

            String category = CATEGORY_MAP.get(dataDiv).getOrDefault(categoryCode, "미분류");
            String subCategory = "P".equals(dataDiv)
                    ? P_SUBCATEGORY_MAP.get(categoryCode).getOrDefault(subCategoryCode, "해당없음")
                    : "00".equals(subCategoryCode) ? "해당없음" : itemName.split(" ")[1];

            String brandName = itemElement.getElementsByTagName("mkrNm").item(0).getTextContent();
            String[] capacityAndUnit = splitNumericAndUnit(itemElement.getElementsByTagName("nutConSrtrQua").item(0).getTextContent());
            String nutriUnit = capacityAndUnit[1];
            int nutriCapacity = Integer.parseInt(capacityAndUnit[0]);
            int nutriKcal = Integer.parseInt(itemElement.getElementsByTagName("enerc").item(0).getTextContent());
            int nutriCarbohydrate = parseNutrient(itemElement, "chocdf");
            int nutriProtein = parseNutrient(itemElement, "prot");
            int nutriFat = parseNutrient(itemElement, "fatce");

            items.add(Item.builder()
                    .itemName(itemName)
                    .category(category)
                    .subCategory(subCategory)
                    .brandName(brandName)
                    .nutriUnit(nutriUnit)
                    .nutriCapacity(nutriCapacity)
                    .nutriKcal(nutriKcal)
                    .nutriCarbohydrate(nutriCarbohydrate)
                    .nutriProtein(nutriProtein)
                    .nutriFat(nutriFat)
                    .build());
        }
        return items;
    }


    // XML 요소에서 영양소 값 파싱(실수 -> 정수)
    private int parseNutrient(Element element, String tagName) {
        String value = element.getElementsByTagName(tagName).item(0).getTextContent();
        return (value.isEmpty() || "0".equals(value)) ? 0 : (int) Math.round(Double.parseDouble(value));
    }


    // 문자열에서 숫자와 단위 분리
    private static String[] splitNumericAndUnit(String str) {
        Pattern pattern = Pattern.compile("([0-9]+)([a-zA-Z]+)");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches() ? new String[]{matcher.group(1), matcher.group(2)} : new String[]{"0", ""};
    }
}
