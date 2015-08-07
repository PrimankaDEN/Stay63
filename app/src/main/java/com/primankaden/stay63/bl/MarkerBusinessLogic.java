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

    /**
     * Encapsulates clustering algorithms
     */
    private class ClusterMatrix {
        private final List<? extends AbsPoint> pointsBefore;
        private SparseArray<ClusterPoint> matrix;
        private int pointsCount;

        ClusterMatrix(List<? extends AbsPoint> pointsBefore) {
            this.pointsBefore = pointsBefore;
            pointsCount = pointsBefore.size();
        }

        private void initMatrix() {
            matrix = new SparseArray<>();
        }

        private static final double STEP_IN_DP = 60;

        private double getStep(double scale) {
            double worldWideInDP = 256 * Math.pow(2, scale);
            return 360 * (STEP_IN_DP / worldWideInDP);
        }

        /**
         * Grid based algorithm
         */
        ClusterMatrix forScale(double scale) {
            double step = getStep(scale);
            initMatrix();
            for (AbsPoint p : pointsBefore) {
                int column = (int) (p.getLongitude() / step);
                int line = (int) (p.getLatitude() / step);
                addTo(p, line, column);
            }
            return this;
        }

        /**
         * Grid based algorithm
         */
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
            int index = getIndex(lineTo, columnTo);
            ClusterPoint cPoint;
            if (matrix.get(index) == null) {
                cPoint = new ClusterPoint();
                matrix.put(index, cPoint);
            } else {
                cPoint = matrix.get(index);
            }
            cPoint.add(p);
        }

        private int getIndex(int line, int column) {
            return pointsCount * line + column;
        }

        public List<AbsMarker> asMarkerList() {
            List<AbsMarker> result = new ArrayList<>();
            ClusterPoint p;
            for (int l = 0; l < matrix.size(); l++) {
                p = matrix.get(matrix.keyAt(l));
                if (p == null || p.isEmpty()) {
                    continue;
                }
                if (p.size() < 2) {
                    result.add(new StopMarker(p.get(0)));
                } else {
                    result.add(new ClusterMarker(p));
                }
            }
            return result;
        }
    }
}
