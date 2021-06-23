package AI.hotel.utils;


import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static AI.hotel.utils.FileUtils.getStaticPath;
import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

@Configuration
@Slf4j
public class FaceCompare {


    @Value("${face.detach.appId}")
    private String appId;

    @Value("${face.detach.sdkKey}")
    private String sdkKey;

    @Bean
    public FaceEngine init() throws IOException {

        File file1 = new File("");
        String filePath = file1.getCanonicalPath() + File.separator + "lib" + File.separator + "WIN64\\";

        FaceEngine faceEngine = new FaceEngine(filePath);

        int errorCode = faceEngine.activeOnline(appId, sdkKey);

        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            log.error("引擎激活失败");
        }

        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
        errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            log.error("获取激活文件信息失败");
        }
        System.out.println(errorCode);

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(10);
        engineConfiguration.setDetectFaceScaleVal(16);
        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);

        //初始化引擎
        errorCode = faceEngine.init(engineConfiguration);

        if (errorCode != ErrorInfo.MOK.getValue()) {
            log.error("初始化引擎失败");
        }
        return faceEngine;
    }

    public FaceFeature getFeatureData(FaceEngine faceEngine, int errorCode, String path) {
        ImageInfo imageInfo = getRGBData(new File(path));
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        FaceFeature faceFeature = new FaceFeature();
        errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature.getFeatureData());
        return faceFeature;
    }
    
}
