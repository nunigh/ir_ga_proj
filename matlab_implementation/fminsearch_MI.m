function [ origScore, improvedScore, improvedTranVec ]= fminsearch_MI( tranVec0, sensedImgPath , referencedImgPath , workingPath, taskID)
fprintf("fminsearch_MI: start\n");
    [ origScore, improvedScore, improvedTranVec ] = fminsearch_wrapper(tranVec0, sensedImgPath , referencedImgPath, @MI_reverse , workingPath, taskID);
fprintf("fminsearch_MI: end\n");   
end

%  [-10.792972, -43.632618, 1.3954129, 1.0046675, 0.9795167, 0.04440333, -0.014209942]