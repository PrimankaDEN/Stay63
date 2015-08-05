package com.primankaden.stay63.bl;

import android.util.SparseArray;

import com.primankaden.stay63.entities.AbsPoint;
import com.primankaden.stay63.entities.ClusterPoint;
import com.primankaden.stay63.entities.FullStop;
import com.primankaden.stay63.entities.marker.AbsMarker;
import com.primankaden.stay63.entities.marker.ClusterMarker;
import com.primankaden.stay63.entities.marker.StopMarker;
import com.primankaden.stay63.entities.marker.UserMarker;

import java.util.ArrayList;
import java.util.List;

public class MarkerBusinessLogic {
    private static final int CLUSTER_WIDTH = 5;
    private static final int CLUSTER_HEIGHT = 6;
    //TODO process dynamically?
//    private static final double MAX_LATITUDE = 53.5715;
    private static final double MIN_LATITUDE = 52.9697;
    //    private static final double MAX_LONGITUDE = 50.5018;
    private static final double MIN_LONGITUDE = 49.7576;
    private static MarkerBusinessLogic instance;

    private MarkerBusinessLogic() {
    }

    public static MarkerBusinessLogic getInstance() {
        if (instance == null) {
            instance = new MarkerBusinessLogic();
        }
        return instance;
    }

    public List<AbsMarker> getPointsBetweenCords(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
        List<FullStop> list = StopBusinessLogic.getInstance().getBetweenCoords(minLatitude, maxLatitude, minLongitude, maxLongitude);
        return new ClusterMatrix(list).betweenCoords(minLatitude, maxLatitude, minLongitude, maxLongitude).asMarkerList();
    }

    public AbsMarker getUserMarker() {
        return new UserMarker(GeoBusinessLogic.getInstance().getCurrentPoint());
    }

    public List<AbsMarker> getPointsForScale(double scale) {
        List<FullStop> list = StopBusinessLogic.getInstance().getAllFullStops();
        return new ClusterMatrix(list).forScale(scale).asMarkerList();
    }

    private class ClusterMatrix {
        private final List<? extends AbsPoint> pointsBefore;
        private SparseArray<SparseArray<ClusterPoint>> matrix;

        ClusterMatrix(List<? extends AbsPoint> pointsBefore) {
            this.pointsBefore = pointsBefore;
        }

        private void initMatrix() {
            matrix = new SparseArray<>();
        }

        private static final double STEP_IN_DP = 60;

        private double getStep(double scale) {
            double worldWideInDP = 256 * Math.pow(2, scale);
            return 360 * (STEP_IN_DP / worldWideInDP);
        }

        ClusterMatrix forScale(double scale) {
            double step = getStep(scale);
            initMatrix();
            for (AbsPoint p : pointsBefore) {
                int column = (int) ((p.getLongitude() - MIN_LONGITUDE) / step);
                int line = (int) ((p.getLatitude() - MIN_LATITUDE) / step);
                addTo(p, line, column);
            }
            return this;
        }

        ClusterMatrix betweenCoords(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
            double latStep = (maxLatitude - minLatitude) / CLUSTER_WIDTH;
            double longStep = (maxLongitude - minLongitude) / CLUSTER_HEIGHT;
            initMatrix();
            for (AbsPoint p : pointsBefore) {
                int column = (int) ((p.getLongitude() - minLongitude) / longStep);
                int line = (int) ((p.getLatitude() - minLatitude) / latStep);
                addTo(p, line, column);
            }
            return this;
        }

        private void addTo(AbsPoint p, int lineTo, int columnTo) {
            SparseArray<ClusterPoint> lineMap;
            if (matrix.get(lineTo) == null) {
                lineMap = new SparseArray<>();
                matrix.put(lineTo, lineMap);
            } else {
                lineMap = matrix.get(lineTo);
            }
            ClusterPoint cPoint;
            if (lineMap.get(columnTo) == null) {
                cPoint = new ClusterPoint();
                lineMap.put(columnTo, cPoint);
            } else {
                cPoint = lineMap.get(columnTo);
            }
            cPoint.add(p);
        }

        public List<AbsMarker> asMarkerList() {
            List<AbsMarker> result = new ArrayList<>();
            SparseArray<ClusterPoint> line;
            for (int l = 0; l < matrix.size(); l++) {
                line = matrix.get(matrix.keyAt(l));
                for (int c = 0; c < line.size(); c++) {
                    ClusterPoint p = line.get(line.keyAt(c));
                    if (p == null || p.isEmpty()) {
                        continue;
                    }
                    if (p.size() < 2) {
                        result.add(new StopMarker(p.get(0)));
                    } else {
                        result.add(new ClusterMarker(p));
                    }
                }
            }
            return result;
        }
    }
}
