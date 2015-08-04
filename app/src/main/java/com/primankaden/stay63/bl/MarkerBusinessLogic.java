package com.primankaden.stay63.bl;

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
    //TODO process dynamically
    private static final double MAX_LATITUDE = 50.5018;
    private static final double MIN_LATITUDE = 49.7576;
    private static final double MAX_LONGITUDE = 53.5715;
    private static final double MIN_LONGITUDE = 52.9697;
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
        private List<? extends AbsPoint> pointsBefore;
        private ClusterPoint points[][];
        private double lastProcessedScale;

        ClusterMatrix(List<? extends AbsPoint> pointsBefore) {
            this.pointsBefore = pointsBefore;
        }

        private static final double STEP_IN_DP = 20;

        private double getStep(double scale) {
            double worldWideInDP = 256 * Math.pow(2, scale);
            return 360 * (STEP_IN_DP / worldWideInDP);
        }

        ClusterMatrix forScale(double scale) {
            double step = getStep(scale);
            int horSize = (int) ((MAX_LONGITUDE - MIN_LONGITUDE) / step);
            int verSize = (int) ((MAX_LATITUDE - MIN_LATITUDE) / step);
            points = new ClusterPoint[verSize+1][horSize+1];
            for (AbsPoint p : pointsBefore) {
                int column = (int) ((p.getLongitude() - MIN_LONGITUDE) / step);
                int line = (int) ((p.getLatitude() - MIN_LATITUDE) / step);
                if (points[line][column] == null) {
                    points[line][column] = new ClusterPoint();
                }
                points[line][column].add(p);
            }
            return this;
        }

        ClusterMatrix betweenCoords(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
            points = new ClusterPoint[CLUSTER_HEIGHT][CLUSTER_WIDTH];
            double latRange = maxLatitude - minLatitude;
            double longRange = maxLongitude - minLongitude;
            for (AbsPoint p : pointsBefore) {
                int column = (int) ((p.getLongitude() - minLongitude) / (longRange / CLUSTER_WIDTH));
                int line = (int) ((p.getLatitude() - minLatitude) / (latRange / CLUSTER_HEIGHT));
                if (points[line][column] == null) {
                    points[line][column] = new ClusterPoint();
                }
                points[line][column].add(p);
            }
            return this;
        }

        public List<AbsMarker> asMarkerList() {
            List<AbsMarker> result = new ArrayList<>();
            for (ClusterPoint[] ps : points) {
                for (ClusterPoint p : ps) {
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
