function [ origScore, improvedScore, improvedTranVec ]= fminsearch_NCC( tranVec0, sensedImgPath , referencedImgPath , workingPath, taskID)
fprintf("fminsearch_NCC: start\n");


    [ origScore, improvedScore, improvedTranVec ] = fminsearch_wrapper(tranVec0, sensedImgPath , referencedImgPath, @NCC_abs , workingPath, taskID);
fprintf("fminsearch_NCC: end\n");   
end

%  [-10.792972, -43.632618, 1.3954129, 1.0046675, 0.9795167, 0.04440333, -0.014209942]