function [ origScore, improvedScore, improvedTranVec ] = fminsearch_wrapper( tranVec0, sensedImgPath , referencedImgPath, fitnessFunction , workingPath, taskID)
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
fprintf("fminsearch_wrapper: start\n");
    imageSaver = ImageSaver(workingPath, taskID);
    sensedImgRead = im2double(imread(sensedImgPath));
    dimSensed = size(size(sensedImgRead));
    if dimSensed(2) == 3
        sensedImgRead = rgb2gray(sensedImgRead);
    end
    referencedImgRead = im2double(imread(referencedImgPath));
    dimRefrenced = size(size(referencedImgRead));
    if dimRefrenced(2) == 3
        referencedImgRead = rgb2gray(referencedImgRead);
    end
    
    imageSaver.do (referencedImgRead,'referenced');
    imageSaver.do (sensedImgRead, 'sensed');
    
    % calculate original tran
    % calculate original HD

    [SensedTr0,SensedTr0Ref] = AffineTran( tranVec0, sensedImgRead ,size(referencedImgRead) );
    imageSaver.do (SensedTr0, 'transformedBeforeOptim');
    imdiff0 = imabsdiff(SensedTr0,referencedImgRead);
    imageSaver.do  (imdiff0 , 'transformedBeforeOptimDiff');
    origScore = fitnessGeneralCalculation (tranVec0,sensedImgRead,referencedImgRead,fitnessFunction);
    %if sum(isnan(origScore(:)))
    %    origScore = origScore(2)
    %    improvedScore = origScore
    %    improvedTranVec = tranVec0
    %    return
    %end
    

    options = optimset('MaxFunEvals',10000);
    improvedTranVec = fminsearch (@(tranVec) fitnessGeneralCalculation( tranVec, sensedImgRead , referencedImgRead ,fitnessFunction ), tranVec0,options);
    [SensedTrRes,SensedTrResRef] = AffineTran( improvedTranVec, sensedImgRead ,size(referencedImgRead) );
    improvedScore = fitnessGeneralCalculation (improvedTranVec,sensedImgRead,referencedImgRead, fitnessFunction);
    imdiffRes = imabsdiff(SensedTrRes,referencedImgRead);
    imageSaver.do  (imdiffRes, 'afterOptimDiff');
    imageSaver.do( SensedTrRes, 'afterOptim');   
    fprintf("fminsearch_wrapper: end\n");
    


end

