function res = padAndHD(img1,img2Trans)
[x1,y1]  = size (img1);
[x2,y2] = size(img2Trans);
if (x2 > x1)
    padx1 = x2 - x1;
    padx2 = 0;
else 
    padx2 = x1 - x2;
    padx1 = 0;
end
if (y2 > y1 )
    pady1 = y2 - y1;
    pady2 = 0;
else
    pady2 = y1 - y2;
    pady1 = 0;
end
pad1=[padx1,pady1];
pad2=[padx2,pady2];

padedImg1 = padarray (img1, pad1,0,'post' );
padedImg2 = padarray (img2Trans, pad2,0,'post' );
res = HausdorffDist (padedImg1, padedImg2);

end

