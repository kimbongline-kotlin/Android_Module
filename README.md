# Android_Module

김봉라인이 외주 프로젝트 진행하며 사용했더 유용한 라이브러리 및 자체 개발 모듈 입니다.

1. InstaTagView : 인스타그램 사진업로드시 장소 및 사용자 태그 기능.
2. HashTagView, HashTagEdit : EditText, TextView #태그 색상 변경
3. KbImage : Glide 라이브러리를 이용한 서버이미지 로드 최적화 뷰.
4. KbPickerActivity : Gallery, Camera 통합 뷰.
5. DisplayUtil : DP to Pixcel 혹은 Pixcel to DP 변화 툴.
6. HashKeyTool : 구글 스토어 Sigining 업로드 시 주어지는 ByteArray 키 값 HashKey로 변경.
7. ArraySaved : PreferenceManager 에 문자 배열 저장 및 가져오는 툴. 
8. ColorHex : #000000 과 같은 Hex값을 int 값 으로 변경해주는 툴.
9. InstallCheck : 외부 앱 설치 유무 확인 해주는 툴.
10. PriceUtil : 금액 및 숫자 표시지 천단위 , 붙여주는 툴.
11. ValidCheck : 이메일 및 비밀번호 패턴 유효성 검사.
12. KbActivity, KbViewModel : MVVM 패턴 사용시 유용학 사용되는 툴.



# 1.InstaTagView
- 인스타그램 사진업로드시 사람 혹은 장소르 태그 할때 이미지의 원하는 위치에 태그를 붙이는 기능입니다.
- 디바이스 기기별 x,y 좌푯값이 저장되며, 태그내용 뿐만아니라 weburl, imageurl 등 다양한 정보를 추가로 저장 가능합니다.

1. Xml에 XXX.kotlin_module.InstagramTagItem.InstaTag 추가.
2. InstaTag의 ImageView.영역은 parent.tagImageView 입니다.

          ex) R.id.instaTagView 라는 InstaTag 뷰가 존재하면 Kotlin에서는 instaTagView.tagImageView 로 ImageView 영역만 불러옰 있습니다.

3. Tag 추가 하기 
 : .add(x : 초기 x 좌푯값 ,y : 초기 y 좌푯값 ,title : 태그에 보여지는 내용. ,info : 태그에 보여지지 않지만 추가되는 내용.) 
ex) instaTagView.add(10,10,'태그 제목','태그 내용')
4. 추가된 Tag 데이터 불러오기
       
          x 좌표 : instaTagView.listOfTagsTobeTagged.get(0).x_co_ord
          y 좌표 : instaTagView.listOfTagsTobeTagged.get(0).y_co_ord
          태그 제목 : instaTagView.listOfTagsTobeTagged.get(0).title
          태그 내용 : instaTagView.listOfTagsTobeTagged.get(0).info
 
 업데이트 사항 : 하나의 이미지에 여러개의 태그르 추가 가능하도로 업데이트 할 예정입니다.

# 2.HashTagView, HashTagEdit
-해시 태그의 색상 및 at, 등 다양한 옵션들의 색상을 적용해주는 EditText, TextView입니다.

         Attribute
         hashTagColor : 해시 태그의 글자색을 지정합니다.
         hashTagBackground : 해시 태그의 배경색을 지정합니다.
         atColor : @의 색상을 지정합니다.
         atBackground : @의 배경색을 지정합니다.
         hashTagSize : HashTag의 크기를 지정합니다.
         atSize : @의 크기를 지정합니다.

1. HashTagView : EditText에 입력과 동시에 해시태그, @, _ 등 다양한 커스텀이 가능합니다.
2. HashTagEdit : EditText에 입력과 동시에 해시태그, @, _ 등 다양한 커스텀이 가능합니다.
3. R.id.XX.hashTags : 해시태그의 개수를 List<String>으로 반환합니다.
4. R.id.XX.ats : @의 개수를 List<String>으로 반환합니다.

# 3.KbImage
- 김봉라인 외주 프로젝트를 진행하며 Glide라이브러리를 이용한 이미지 최적화 로딩툴입니다.
- Url 에는 내장 이미지(R.drwable.xx), url 이미지 모두 사용가능합니다. 

         KbImage.gifimage(url, ImageView) : GIF이미지 로딩. 
         KbImage.image(url, ImageView) : 일반 이미지 로딩.
         KbImage.cornerImage(url, corner_radius,ImageView ) : 둥근 모서리 이미지 로딩. (corner_radius : Int값 모서리 둥근 정도.)
         KbImage.circleImage(url,ImageView) : 원형 이미지 로딩.

# 4.KbPickerActivity 
- 갤러리, 카메라가 결합된 액티비티입니다. 내장 이미지 혹은 촬영을 통해 이미지를 저장할 수 있습니다.
- 권한 설정은 박상권님의 TedPermission을 이용하였습니다. (https://github.com/ParkSangGwon/TedPermission)

1. CameraValue.TYPE : 이미지 한장 혹은 여러장을 선택합니다. (Type-SINGLE : 한장,MULTI : 여러장)
2. CameraValue.MAX_VALUE : 여러장 이미지 선택시 최대 갯수를 선택합니다. (기본값 1)
3. CameraValue.RESULT : onActivityResult에서 Call 하기 위한 값입니다.

         KbPickerCall.open(this@HomeActivity,applicationContext,CameraValue.MULTI,10) 
         : 여러장 이미지 선택(기본), 최대 10장 선택)

         onActivityResult(…) {
              If(requestCode == CameraValue.RESULT …)	  
         }

# 5.DisplayUtl
- DP 값을 Pixcel로 혹은 Pixcel값을 DP로 변환하는 툴 입니다.

         Convert.toPixcel(dp,context) -> Float : DP값을 Pixcel로 변환.
         Convert.toDp(picxel,context) -> Float : Pixce값을 DP로 변환.

# 6.HashKeyTool
- 구글 Singing 및 ByteArray로 된 값을 HashKey로 변경해야 할 경우 유용하게 사용합니다.
 
         HashKeyTool(byteArray) -> String 값으로 변환됩니다.

# 7.ArraySaved
- PreferenceManager에 문자 배열을 저장하고 가져오는 툴입니다.

         ArraySaved.saveItem(context, key,ArrayList<string>) :  key 값에 배열 데이터를 저장하는 함수입니다.
         ArraySaved.calledItem(context, key, ArrayList<String>) : key 값으로 저장된 배열 데이터를 가져오는 함수입니다.

# 8.ColorHex
- Hex로된 컬러 값을 int로 변경하여 쉽게 사용하게 도와줍니다.

         ColorHex.set(“#ffffff”)

# 9.InstallCheck
- 디바이스에 설치된 앱을 판단해 줍니다.

         InstallCheck.PackageName(context, com.xx.xxx) : true -> 설치됨, false -> 설치되어있지 않음.

# 10.PriceUtil
- 금액 및 숫자 표시 시에 천단위 , 를 찍어주는 툴입니다.

         PriceUtil.set(string) -> String
         PriceUtil.set(“100000”) -> 10,000

# 11.ValidCheck
- 이메일 및 비밀번호 패턴의 유효성을 검사해주는 툴입니다.

         ValidCheck.email(email : String) -> True or False
         ValidCheck.passpattern(password : String, pattern : String) -> True or False