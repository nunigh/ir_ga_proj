function [ origScore, improvedScore, improvedTranVec ] = fminsearch_HausdorffDist( tranVec0,featuresReferenced, featuresSensed , limits , workingPath, taskID)


%function [ origScore, improvedScore, improvedTranVec ] = fminsearch_HausdorffDist( tranVec0, sensedImgPath , referencedImgPath , workingPat, taskID)

%
fprintf("V2\n");
fprintf(" fminsearch_HausdorffDist: start\n");
%save hd_api_vec tranVec0;
%save hd_api_fetref featuresReferenced;
%save hd_api_fetsen featuresSensed;
%save hd_api_lim limits;
origScore = myHD( tranVec0, featuresReferenced , featuresSensed, limits);
improvedTranVec = fminsearch ( @(tranVect) myHD (tranVect,featuresReferenced , featuresSensed,limits), tranVec0 );
improvedScore = myHD( improvedTranVec, featuresReferenced , featuresSensed, limits);

fprintf("fminsearch_HausdorffDist: end\n");   
end

%  [-10.792972, -43.632618, 1.3954129, 1.0046675, 0.9795167, 0.04440333, -0.014209942]