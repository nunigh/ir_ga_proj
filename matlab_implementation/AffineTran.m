function [SensedTransformed,SensedTransformedRef] = AffineTran( tranVec, sensedImg ,referencedImgSize )
%UNTITLED3 Summary of this function goes here
%   Detailed explanation goes here
    tx  = tranVec(1);
    ty  = tranVec(2);
    rot = tranVec(3)* (-1); % don't know why need to multiple by -1, but it seems to work
    scaleX  = tranVec(4);
    scaleY  = tranVec(4);
    shearX  = tranVec(6);
    shearY  = tranVec(7);
    
   % muliple by -1 to do in anti clockwise
    %SensedTransformed = imrotate(sensedImg,rot*(-1),'nearest');%,'crop');
    %maybe the rotation is better to be before the other transformations
    
    TransitionMatrix  = [1 0 0;  0 1 0; tx ty 1];
    ScaleMtrix  = [scaleX 0 0 ; 0 scaleY 0 ;0 0 1];
    %ScaleMtrix  = [scaleX 0 0 ; 0 scaleY 0 ;0 0 1];
    %ShearMatrix = [1 shearY 0 ; shearX 1 0 ;0 0 1];
    ShearXMatrix = [1 0 0 ; shearX 1 0 ;0 0 1];
    ShearYMatrix = [1 shearY 0 ; 0 1 0 ;0 0 1];
    RotationMatrix  = [cosd(rot) sind(rot) 0; -sind(rot) cosd(rot) 0; 0 0 1 ];
    % the order of the operations matter
    fullTransformationMatrix = TransitionMatrix*RotationMatrix*ScaleMtrix*ShearXMatrix*ShearYMatrix;
    %fullTransformationMatrix = TransitionMatrix*ScaleMtrix*ShearMatrix*RotationMatrix;
    affineTranformation = affine2d (fullTransformationMatrix);
    refImgRef = imref2d(referencedImgSize);
    [SensedTransformed,SensedTransformedRef] = imwarp (im2double(sensedImg), affineTranformation, 'nearest', 'OutputView',refImgRef,'FillValues', Inf );
  
end

