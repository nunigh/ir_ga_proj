function  printTransformation( tranVec0, imgPath , workingPath, taskID)
    imageSaver = ImageSaver(workingPath, taskID);
    imgRead = imread(imgPath);
    dimSensed = size(size(imgRead));
    if dimSensed(2) == 3
        imgRead = rgb2gray(imgRead);
    end
    [SensedTr0,SensedTr0Ref] = AffineTran( tranVec0, imgRead ,size(imgRead) );
    imageSaver.do (SensedTr0, 'matlab_transformation_result');
end
