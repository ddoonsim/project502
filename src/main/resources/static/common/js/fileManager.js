var commonLib = commonLib || {} ;
/**
* 1. 파일 업로드
*/
commonLib.fileManager = {
    /**
    * 파일 업로드 처리
    */
    upload(files) {
        try {
            if (!files || files.length == 0) {
                throw new Error("⚠️업로드할 파일을 선택하세요.") ;
            }

            // gid 값 가져오기
            const gidEl = document.querySelector("[name='gid']") ;
            if (!gidEl || !gidEl.value.trim()) {
                throw new Error("⚠️gid가 누락되었습니다.") ;
            }

            const gid = gidEl.value.trim() ;

            // 자바스크립트에서 양식을 전달하는 방식, file에 특화되어 있음(기본 Content-Type:multipart/form-data)
            const formData = new FormData() ;
            formData.append("gid", gid) ;

            for (const file of files) {
                formData.append("file", file) ;
            }

            const { ajaxLoad } = commonLib ;
            ajaxLoad("POST", "/api/file", formData, "json") ;

        } catch (err) {
            alert(err.message) ;
            console.error(err) ;
        }
    }
};

// 이벤트 처리
window.addEventListener("DOMContentLoaded", function() {
    const uploadFiles = document.getElementsByClassName("upload_files") ;
    const fileEl = document.createElement("input") ;
    fileEl.type = "file" ;
    fileEl.multiple = true ;    // 여러 개의 파일을 선택 가능하게

    // 파일 업로드 버튼 클릭 처리 --> 파일 탐색기 열기
    for (const el of uploadFiles) {
        // 파일 탐색기 창 띄우기
        el.addEventListener("click", function() {
            fileEl.click() ;
        });
    }

    // 파일 선택 시 이벤트 처리
    fileEl.addEventListener("change", function(e) {
        //console.dir(e.target.files) ;    // 파일에 대한 정보 콘솔창에서 확인
        CommonLib.fileManager.upload(e.target.files) ;
    });
});